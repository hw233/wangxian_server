<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,
    com.sqage.stat.commonstat.manager.Impl.*,jxl.format.UnderlineStyle,
	jxl.write.WritableFont,jxl.Workbook,java.net.*"
	%>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String huoBiType=request.getParameter("huoBiType");
	String fenqu=request.getParameter("fenqu");
	String wupintype=request.getParameter("wupintype");
	String gamelevel = request.getParameter("gamelevel");
	String username = request.getParameter("username");
	   
	   String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	 String spNumber="daoJuXiaoHao"+startDay+endDay; 
	   
	if("0".equals(huoBiType)){huoBiType=null;}
	if("0".equals(fenqu)){fenqu=null;}
	if("0".equals(wupintype)){wupintype=null;}
	if(gamelevel == null){ gamelevel = "全部";}else{spNumber+=gamelevel;}
	if(username == null){ username = "全部";}else{spNumber+=username;}
	
              DaoJuManagerImpl daoJuManager=DaoJuManagerImpl.getInstance();
               String lost="LOST','发送邮件删除','合成删除','鉴定删除','铭刻删除','强化删除','商店出售删除','死亡掉落";
              String gamelevel_search=null;
              String username_search=null;
              if(!"全部".equals(username)){username_search=username;}
              if(!"全部".equals(gamelevel)){gamelevel_search=gamelevel;}
              spNumber+=fenqu+huoBiType+wupintype;
		      List<String[]> goumaiList=daoJuManager.getDaoJuXiaoHao(startDay,endDay,fenqu,huoBiType,gamelevel_search,wupintype,lost,username_search);
		      
		  String basePath =getServletContext().getRealPath("/")+"excel";
          File thePath = new File(basePath);
          if (thePath.isDirectory() == false)// 如果该目录不存在，则生成新目录
          {
              thePath.mkdirs();
          }
         // String xls= basePath+"\\"+spNumber+".xls";
          String xls= basePath+"/"+spNumber+".xls";
          File f=new File(xls);
          jxl.write.WritableFont wfc = new jxl.write.WritableFont(WritableFont.ARIAL,6, WritableFont.NO_BOLD, false,
                     UnderlineStyle.NO_UNDERLINE);
          jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(wfc);
                  wcfFC.setWrap(true);   
          jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(f);
          
              
              String sheet = "道具消耗";
              jxl.write.WritableSheet ws = wwb.createSheet(sheet, 0);
              jxl.write.Label labelC = new jxl.write.Label(0, 0, "物品名称");
              ws.addCell(labelC);
              //labelC = new jxl.write.Label(1, 0, "物品单价（元宝）");
              //ws.addCell(labelC);
              labelC = new jxl.write.Label(1, 0, "购买数量");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(2, 0, "总价格（文）");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(3, 0, "占比");
              ws.addCell(labelC);
             
                Long count=0L;
					for(int i = 0 ; i < goumaiList.size() ; i++){
					String [] goumai=(String[])goumaiList.get(i);
					count+=Long.parseLong(goumai[2]);
					}
             
              for(int j = 0; j < goumaiList.size();j++){
                  String[] goumai = (String[])goumaiList.get(j);
                  Long zhanbi=0L;
					if(count!=0){zhanbi=(Long.parseLong(goumai[2])*100)/count;}
                  labelC = new jxl.write.Label(0, j+1, goumai[0]);
                  ws.addCell(labelC);
                  //labelC = new jxl.write.Label(1, j+1, goumai[1]);
                  //ws.addCell(labelC);
                  labelC = new jxl.write.Label(1, j+1, goumai[2]);
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(2, j+1, goumai[3]);
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(3, j+1, zhanbi.toString()+"%");
                  ws.addCell(labelC);
              }
          wwb.write();
          wwb.close();
		
		
	response.reset();
    response.setContentType("binary/octet-stream");
    String filenamedownload ="/excel/"+spNumber+".xls";//即将下载的文件的相对路径
    String filenamedisplay = spNumber+".xls";//下载文件时显示的文件保存名称
    
    
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
		
		      
     %>
