package com.xuanzhi.tools.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*; 

import java.net.*;

import java.lang.reflect.*;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.watchdog.ConfigFileChangedAdapter;
import com.xuanzhi.tools.watchdog.ConfigFileChangedListener;

import org.apache.log4j.*;

/**
 * Servlet Fileter
 */
public class AuthorizedServletFilter implements Filter,ConfigFileChangedListener
{
	private FilterConfig filterConfig;
	
	private Hashtable<String,String> userMap = new Hashtable<String,String>(); 
	
	private Hashtable<String,Properties> userPropsMap = new Hashtable<String,Properties>(); 
	
	private String title = "";
	
	public void init(FilterConfig config)
	{
		this.filterConfig = config;
		String filePath = config.getInitParameter("authorize.file");
		filePath = config.getServletContext().getRealPath(filePath);
		title = config.getInitParameter("title");
		if(title == null) title = "";
		if(filePath != null){
			File f = new File(filePath);
			ConfigFileChangedAdapter c = ConfigFileChangedAdapter.getInstance();
			c.addListener(f,this);
		}
		load(filePath);
	}

	protected void load(String filePath){
		Hashtable<String,String> m = new Hashtable<String,String>(); 
		Hashtable<String,Properties> mm = new Hashtable<String,Properties>(); 
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String s = null;
			while( (s = reader.readLine()) != null){
				s = s.trim();
				if(s.startsWith("#") || s.startsWith("!") || s.length() == 0){
					continue;
				}
				if(s.length() > 0 && s.charAt(0) == '.'){
					s = s.substring(1);
					if(s.indexOf("=") > 0){
						String key = s.substring(0,s.indexOf("="));
						String value = s.substring(s.indexOf("=")+1).trim();
						int k = key.indexOf(".");
						if(k > 0){
							String userName = key.substring(0,k).trim();
							key = key.substring(k+1).trim();
							if(m.containsKey(userName) && key.length() > 0){
								Properties props = mm.get(userName);
								if(props == null){
									props = new Properties();
									mm.put(userName,props);
								}
								props.setProperty(key,value);
							}
						}
					}
				}else{
					String cols[] = s.split(",");
					if(cols.length > 1){
						m.put(cols[0],cols[1]);
					}
				}
			}
			reader.close();
			userMap = m;
			userPropsMap = mm;
		}catch(IOException e){
			System.out.println("[WARN] [load-authorize-file] [error] ["+filePath+"]");
			e.printStackTrace(System.out);
		}
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
	 public static final String PROPERTIES = "authorize.properties";
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
    	 request.setCharacterEncoding(System.getProperty("file.encoding","gbk"));
    	 
    	 HttpSession session = request.getSession();
    	 String username = request.getParameter(USERNAME);
		 String password = request.getParameter(PASSWORD);
		 
    	 if(username == null){
    		 username = (String)session.getAttribute(USERNAME);
    		 password = (String)session.getAttribute(PASSWORD);
    	 }
    	 
    	 if(username == null){
    		 response.setContentType("text/html;charset=UTF-8");
    		 response.setCharacterEncoding("UTF-8");
    		 ServletOutputStream out = response.getOutputStream();
    		 String pageContent = getLoginPage("","");
    		 out.write(pageContent.getBytes());
    		 out.flush();
    		 return;
    	 }else{
    		 String passwd = userMap.get(username);
    		 if(passwd == null || passwd.equals(password)==false ){
        		 response.setContentType("text/html;charset=UTF-8");
        		 response.setCharacterEncoding("UTF-8");
    			 ServletOutputStream out = response.getOutputStream(); 
    			 String pageContent = getLoginPage("登录失败，用户名和密码匹配失败",username);
        		 out.write(pageContent.getBytes());
        		 out.flush();
        		 return;
    		 }
    	 }
    	 
    	 session.setAttribute(USERNAME,username);
		 session.setAttribute(PASSWORD,password);
		 if(userPropsMap.containsKey(username)){
			 session.setAttribute(PROPERTIES,userPropsMap.get(username));
		 }else{
			 session.setAttribute(PROPERTIES,new Properties());
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

     public String getLoginPage(String message,String username){
    	 if(username == null) username = "";
    	 StringBuffer sb = new StringBuffer();
    	 sb.append("<html xmlns='http://www.w3.org/1999/xhtml'><head><link href='css/ltcss.css' rel='stylesheet' type='text/css' /><meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
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
     
     public void fileChanged(File file) {
		load(file.getAbsolutePath());
	 }
}
