<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*,jxl.format.UnderlineStyle,jxl.write.WritableFont,jxl.Workbook,java.net.*"
	%>
	<%! 
	 /**
        * 把字符串续写到目标文件里，在原来文件里追加。
        */
        public boolean writing(String cont, File dist) {
         try {
          BufferedWriter writer = new BufferedWriter(new FileWriter(dist,true));
          writer.write(cont);
          writer.flush();
          writer.close();
          return true;
         } catch (IOException e) {
          e.printStackTrace();
          return false;
         }
        }
	 %>
    <%
     String flag = request.getParameter("flag");
	 String startDay = request.getParameter("day");
	 if(startDay == null)  startDay = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	
     if ("true".equals(flag)) {  //如果是刚打开页面，不产生下载文件
      
      ///////////////////////下载文件 ////////////////////////////////
        String spNumber="djQd"+DateUtil.formatDate(new Date(),"HHmmss");
        System.out.println("spNumber:"+spNumber);
        String basePath =getServletContext().getRealPath("/")+"excel";
          File thePath = new File(basePath);
          if (thePath.isDirectory() == false)// 如果该目录不存在，则生成新目录
          {
              thePath.mkdirs();
          }
         // String xls= basePath+"\\"+spNumber+".xls";
          String xls= basePath+"/"+spNumber+".txt";
          File f=new File(xls);
          
         System.out.println("f.exists():"+f.exists());
         if(!f.exists()){
         DaoJuManagerImpl daoJuManager=DaoJuManagerImpl.getInstance();
         String sql="select * from stat_daoju d where d.fenqu='傲剑凌云' and to_char(d.createdate,'YYYY-MM-DD')='"+startDay+"'";
         List<DaoJu> ls=daoJuManager.getBySql(sql);
         System.out.println("ls.size():"+ls.size());
              for(int j = 0; j < ls.size();j++){
                  DaoJu daoJu=(DaoJu)ls.get(j);
                  String contentStr=daoJu.getDaoJuColor()+"\t"+daoJu.getDaoJuName()+"\t"+daoJu.getDaoJuLevel()+"\t"+daoJu.getFenQu()+"\t"
                  +daoJu.getGetType()+"\t"+daoJu.getPosition()+"\t"+daoJu.getDanJia()+"\t"+daoJu.getDaoJuNum()+"\t"+daoJu.getCreateDate()+"\t"
                  +daoJu.getGameLevel()+"\r\n";
                  writing(contentStr,f);
              }
              }
         // request.setAttribute("num",spNumber);
         // return mapping.findForward("download");	
        
	//String num = request.getAttribute("num").toString();
    //response.setContentType("application/x-download");//设置为下载application/x-download
    response.reset();
    response.setContentType("binary/octet-stream");
    String filenamedownload ="/excel/"+spNumber+".txt";//即将下载的文件的相对路径
    String filenamedisplay = spNumber+".txt";//下载文件时显示的文件保存名称
    
    if(request.getHeader("User-Agent").toLowerCase().indexOf("firefox")>0){
    filenamedisplay=new String(filenamedisplay.getBytes("UTF-8"),"ISO8859-1");//firefox 解决中文名字乱码
    }
    else if(request.getHeader("User-Agent").toUpperCase().indexOf("MSIE")>0){
    filenamedisplay = URLEncoder.encode(filenamedisplay,"UTF-8");  //IE 解决中文名字乱码
    }
    response.addHeader("Content-Disposition","attachment;filename=" +filenamedisplay);
    
    
    
   	out.clear();
	out = pageContext.pushBody();
    try
    {
        RequestDispatcher dispatcher = application.getRequestDispatcher(filenamedownload);
        if(dispatcher != null)
        {
            dispatcher.forward(request,response);
        }
       response.flushBuffer();
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    finally
    {
   
    }		   
      
     }
  %>
		
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" href="../css/style.css" />
		<link rel="stylesheet" href="../css/atalk.css" />
		<script language="javascript" type="text/javascript" src="../js/flotr/lib/prototype-1.6.0.2.js"></script>
		<script language="javascript" type="text/javascript" src="../js/flotr/flotr-0.2.0-alpha.js"></script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;"> 
			道具清单下载 （傲剑凌云）
		</h2>
		<form name="form1" id="form1" method="post" >
		<input type='hidden' name='flag' value='true'/>
		下载统计日期：<input type='text' name='day' value='<%=startDay %>'>(格式：2012-02-09)
		  <br/><br/>
		    		<input type='submit' value='提交'>
		    		<br/><br/>
		    		<font color='red'> 本功能查询数据量较大，请尽量做到不过于频繁使用本功能，尤其要避免多人同时频繁使用本功能。否则，因此导致的后果自负。</font>
		    		<br/><br/>
		    		
		    		</form>
		</center>
		<br>
		  <i>道具汇总下载页面</i>
		<br>
	</body>
</html>
