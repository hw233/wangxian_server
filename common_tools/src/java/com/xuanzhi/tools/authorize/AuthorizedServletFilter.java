package com.xuanzhi.tools.authorize;

import java.io.*;
import java.net.URL;

import javax.servlet.*;
import javax.servlet.http.*; 

import org.apache.log4j.Logger;

/**
 * Servlet Fileter
 */
public class AuthorizedServletFilter implements Filter
{
	
	static Logger logger = Logger.getLogger(AuthorizedServletFilter.class);
	
	private FilterConfig filterConfig;
	
	private String title = "";
	
	private String charset = "utf-8";
	
	private AuthorizeManager authorizeManager = null;
	
	private String GET_AUTH_KEY = "D989JDWJED9OKCOWI5943430KDOCJ90JDW90IS0134FC";
	
	public void init(FilterConfig config)
	{
		long start = System.currentTimeMillis();
		this.filterConfig = config;
		String filePath = config.getInitParameter("authorize.file");
		title = config.getInitParameter("title");
		String proxy = config.getInitParameter("proxy");
		charset = System.getProperty("file.encoding", "utf-8");
		if(title == null) title = "";
		authorizeManager = AuthorizeManager.getInstance();
		
		if(authorizeManager == null){
			authorizeManager = new AuthorizeManager();
			authorizeManager.setAutoLoad(true);
			if(proxy != null) {
				String proxyHost = proxy.split(":")[0];
				int proxyPort = Integer.valueOf(proxy.split(":")[1]);
				authorizeManager.setProxyHost(proxyHost);
				authorizeManager.setProxyPort(proxyPort);
			}
			try {
				if(filePath != null && filePath.toLowerCase().startsWith("http://")){
					//URL url = new URL(filePath + "/" + GET_AUTH_KEY);
					URL url = new URL(filePath);
					authorizeManager.setConfigURL(url);
				}else{
					File file = new File(filePath);
					if(!file.exists()){
						file = new File(config.getServletContext().getRealPath(filePath));
					}
					authorizeManager.setConfigFile(file);
				}
				
				authorizeManager.init();
			} catch (Exception e) {
				System.out.println("Init com.linktone.tools.authorize.AuthorizeManager error:");
				e.printStackTrace(System.out);
				logger.error("Init com.linktone.tools.authorize.AuthorizeManager error",e);
			}

		}
		System.out.println("[AuthorizedServletFilter初始化完成] [proxy:"+(proxy!=null?(authorizeManager.getProxyHost()+":"+authorizeManager.getProxyPort()):"不使用")+"] [file:"+authorizeManager.getConfigFile()+"] [url:"+authorizeManager.getConfigURL()+"] ["+(System.currentTimeMillis()-start)+"ms]");
	}

	
	public void destroy()
	{
	}
	
	public void setFilterConfig(FilterConfig config)
	{
		init(config);
	}

	/**
	 * Return the FilterConfig for this Filter.
	 */
	public FilterConfig getFilterConfig()
	{
	     return this.filterConfig ;
	}
	 
	 /**
	  * The doFilter method of the Filter is called by the container 
	  * each time a request/response pair is passed through the chain 
	  * due to a client request for a resource at the end of the chain. 
	  * The FilterChain passed in to this method allows the Filter to pass on 
	  * the request and response to the next entity in the chain.
	  * 
	  */
	 public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)
		 throws IOException ,ServletException
	 {
		 doFilter((HttpServletRequest)request,(HttpServletResponse)response,chain);
	 }

	 public static final String USERNAME = "authorize.username";
	 public static final String PASSWORD = "authorize.password";
	 //public static final String ENCRYPT_PASSWORD = "authorize.encrypt.password";
	 public static final String USER = "authorize.USER";
	 public static final String PLATFORM = "authorize.PLATFORM";
	 public static final String PROPERTIES = "authorize.properties";
	 public static final String AUTHORIZE_MANAGER = "authorize.manager";
	 public static final String SIGNED_PASSWORD = "authorize.signed.password";
	 
	 
	 /**
	  * The doFilter method of the Filter is called by the container 
	  * each time a request/response pair is passed through the chain 
	  * due to a client request for a resource at the end of the chain. 
	  * The FilterChain passed in to this method allows the Filter to pass on 
	  * the request and response to the next entity in the chain.
	  */
     public void doFilter(HttpServletRequest request,HttpServletResponse response,FilterChain chain)
		 throws IOException ,ServletException
	 {
    	 
    	 request.setCharacterEncoding(charset);
    	 
    	 response.setContentType("text/html;charset=" + charset);
		 response.setCharacterEncoding(charset);
		 
		 //response.setHeader("contentType", "text/html; charset=" + charset);
    	 String remoteAddress = request.getRemoteAddr();
   	 
    	 if(authorizeManager.getConfigFile() != null && request.getRequestURI().indexOf("get_authorize_file") != -1){
    		 ServletOutputStream out = response.getOutputStream();
    		 byte [] pageContent = getAuthorizeFile();
    		 out.write(pageContent);
    		 out.flush();
    		 return;
    	 } 
    	 
    	 //System.out.println("[doFilter] ["+request.getRequestURI()+"]");
    	 
    	 if(request.getRequestURI().indexOf("saving_notifier") == -1) {
	    	 //不对充值页进行验证
	    	 User user = null;
	    	 HttpSession session = request.getSession();
	    	 String username = request.getParameter(USERNAME);
			 String password = request.getParameter(PASSWORD);
			// String encryptPassword = request.getParameter(ENCRYPT_PASSWORD);
			 String signedPassword = request.getParameter(SIGNED_PASSWORD);
			 String url = request.getRequestURL().toString();
			 String queryString = request.getQueryString();
			 if(queryString != null){ 
				 url += "?" + queryString;
			 }
	    	 if(username == null){
	    		 user = (User)session.getAttribute(USER);
	    		 if(user != null){
	    			 username = user.getName();
	    			 password = user.getPassword();
	    		 }
	    	 }
	    	 
	    	 if(username == null){
	    		 ServletOutputStream out = response.getOutputStream();
	    		 String pageContent = getLoginPage("","");
	    		 out.write(pageContent.getBytes());
	    		 out.flush();
	    		 return;
	    	 }else if(user == null){
	    		 UserManager um = authorizeManager.getUserManager();
	             boolean isMatchUser = um.matchUser(username, password) ;
	             //if(!isMatchUser) isMatchUser = um.matchUser2(username, encryptPassword);
	             if(!isMatchUser && signedPassword != null && signedPassword.length() > 0) {
	            	 isMatchUser = um.signedMatchUser(username, signedPassword, request.getRemoteAddr());
	             }
	    		 if(!isMatchUser){
	    			 ServletOutputStream out = response.getOutputStream(); 
	    			 String pageContent = getLoginPage("登录失败，用户名和密码匹配失败",username);
	        		 out.write(pageContent.getBytes());
	        		 out.flush();
	        		 if(logger.isInfoEnabled())
	        			 logger.info("[access-deny] ["+username+"] ["+remoteAddress+"] [登录失败，用户名和密码匹配失败] ["+url+"]");
	        		 
	        		 return;
	    		 }else {
	    			 user = um.getUser(username);
	    			 if(System.currentTimeMillis() > user.getPasswordExpiredTime().getTime()){
	    				 ServletOutputStream out = response.getOutputStream(); 
	    				 String pageContent = getLoginPage("登录失败，密码已经过期，请登录<a href='http://116.213.142.183:8882/game_gateway/admin/'>这里</a>，在《修改密码》->《修改密码》中重新设置密码！",username);
	    				 out.write(pageContent.getBytes());
	    				 out.flush();
	    				 
	    				 if(logger.isInfoEnabled())
	    					 logger.info("[access-deny] ["+username+"] ["+remoteAddress+"] [登录失败，密码已经过期] ["+url+"]");
	    				 
	    				 return;
	    			 }
	    		 }
	    		 user = um.getUser(username);
	    	 }else{
	    		 user = authorizeManager.getUserManager().getUser(username);
	    	 }
	    	 
	    	 PlatformManager pm = authorizeManager.getPlatformManager();
	    	 Platform platform = pm.getPlatformByUrl(url);
	    	 if(platform == null){
	    		ServletOutputStream out = response.getOutputStream(); 
	    		String pageContent = getLoginPage("URL["+url+"]匹配不到任何平台配置，所以您没有访问此页面的权限",username);
	        	out.write(pageContent.getBytes());
	        	out.flush();
	        	
	        	if(logger.isInfoEnabled())
	    			logger.info("[access-deny] ["+username+"] ["+remoteAddress+"] [URL匹配不到任何平台配置] ["+url+"]");
	        	
	        	return;
	    	 }
	    	 Role roles[] = user.getRoles();
	    	 if(roles.length == 0){
	    		 ServletOutputStream out = response.getOutputStream(); 
	    		String pageContent = getLoginPage("用户["+username+"]没有配置任何权限角色，不能访问此平台["+platform.getName()+"]",username);
	        	out.write(pageContent.getBytes());
	        	out.flush();
	        	
	        	if(logger.isInfoEnabled())
	    			logger.info("[access-deny] ["+username+"] ["+remoteAddress+"] [没有配置任何权限角色] ["+url+"]");
	        	
	        	return;
	    	 }
	    	 
	    	 
	    	 
	    	 boolean canAccess = authorizeManager.isPlatformAccessEnable(user,url,remoteAddress);
	    	 
	    	 if(!canAccess){
	    		 ServletOutputStream out = response.getOutputStream(); 
	    		 String pageContent = getLoginPage("对不起，访问失败，用户["+username
	    				 +"]没有权限访问此页面！如果您对此有疑问，请确认是否在办公室访问或者请联系管理员！或者点击"
	    				 +"<a href='http://116.213.142.183:8882/game_gateway/admin/authorize/test.jsp?"+USERNAME+"="
	    				 +username+"&"+PASSWORD+"="+password+"&submitted=true&name="+username
	    				 +"&url="+java.net.URLEncoder.encode(url,charset)
	    				 +"&ip="+remoteAddress+"' target='_blank'>这里</a>来查看具体原因！",username);
	        	 out.write(pageContent.getBytes());
	        	 out.flush();
	        	 
	        	 if(logger.isInfoEnabled())
	    			logger.info("[access-deny] ["+username+"] ["+remoteAddress+"] [没有权限访问此页面] ["+url+"]");
	        	 
	        	 return;
	    	 }
	    	 session.setAttribute(AUTHORIZE_MANAGER, authorizeManager);
	    	 session.setAttribute(USER, user);
	    	 session.setAttribute(PLATFORM, platform);
	    	 session.setAttribute(USERNAME,username);
	    	 //session.setAttribute(ENCRYPT_PASSWORD,encryptPassword);
			 session.setAttribute(PROPERTIES,user.getProperties());
			 
			 if(logger.isInfoEnabled())
	    			logger.info("[access-accept] ["+username+"] ["+remoteAddress+"] ["+user.getRealName()+"] ["+url+"]");
    	 } else {
    		 System.out.println("[不需要验证] ["+request.getRequestURI()+"]");
    	 }
			 
		try
		{
			chain.doFilter(request,response);
		}
		catch(ServletException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new ServletException(e);
		}
		finally
		{
		}
	 }

     public byte[] getAuthorizeFile() throws IOException{
    	 File file = authorizeManager.getConfigFile();
    	 FileInputStream input = new FileInputStream(file);
    	 int len = (int)file.length();
    	 byte bytes[] = new byte[len];
    	 input.read(bytes);
    	 input.close();
    	 return bytes;
     }
     
     public String getLoginPage(String message,String username){
    	 if(username == null) username = "";
    	 StringBuffer sb = new StringBuffer();
    	 sb.append("<html xmlns='http://www.w3.org/1999/xhtml'><head><link href='css/ltcss.css' rel='stylesheet' type='text/css' /><meta http-equiv='Content-Type' content='text/html; charset="+charset+"' />");
    	 sb.append("<script>function txcheck(){	if (document.getElementById('"+USERNAME+"').value==''){		alert('用户名不能为空！请填写完整！');		document.getElementById('"+USERNAME+"').focus();		return false;	}	if (document.getElementById('"+PASSWORD+"').value==''){		alert('密码不能为空！请填写完整！');		document.getElementById('"+PASSWORD+"').focus();		return false;	}	return true;}</script>");
    	 sb.append("</head><body><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>");
    	 if(title != null && title.length() > 0){
        	 sb.append("<center><span style='font-size: 20px;font-weight: bold;color: #000000;'>"+title+"</span></center><br/><br/>");
    	 }
    	 if(message != null && message.length() > 0){
    		 sb.append("<table width='40%' border='2' align='center' cellpadding='0' cellspacing='0' bordercolor='#FF0000'><tr><td height='30' align='center'><span style='font-size: 14px;font-weight: bold;color: #FF0000;'>"+message+"</span></td>  </tr></table>");
    	 }
    	 sb.append("<br/>");
    	 sb.append("<form id='form1' name='form1' method='post' onsubmit='return txcheck();'>");
    	 sb.append("<table width='40%' border='1' align='center' cellpadding='0' cellspacing='1' bordercolor='#CCCCCC'>");
    	 sb.append("<tr><td height='30' colspan='2' align='center' bgcolor='#BADAFE' style='font-style:inherit; font-size:14px; font-weight:bold'>用 户 登 录</td></tr>");
    	 sb.append(" <tr> <td width='30%' height='25' bgcolor='#DDEDFF'><div align='right'>用户名：</div></td>    <td bgcolor='#DDEDFF'><input type='text' name='"+USERNAME+"' id='"+USERNAME+"' value='"+username+"'/>    <span class='redDot'>*必填</span></td>  </tr>");
    	 sb.append("<tr> <td height='25' bgcolor='#DDEDFF'><div align='right'>密&nbsp;&nbsp;码：</div></td>    <td bgcolor='#DDEDFF'><input type='password' name='"+PASSWORD+"' id='"+PASSWORD+"' />   <span class='redDot'>*必填</span></td>  </tr>");
    	 sb.append(" <tr><td height='30' colspan='2' align='center' bgcolor='#BADAFE'><input type='submit' name='button2' id='button2' value='-登录-' />	&nbsp;<input type='reset' name='button' id='button' value='-重置-' /></td>  </tr>");
    	 sb.append(" </table></form></body></html>");
    	 
    	 return sb.toString();
     }
}
