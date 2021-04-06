<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
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
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	String spNumber="huoBiMingXiTongJi"+startDay+endDay; 
	
	if("0".equals(huoBiType)){huoBiType=null;}else{ spNumber+=huoBiType; }
	if("0".equals(fenqu)){fenqu=null;}else{         spNumber+=fenqu; }
	if("0".equals(wupintype)){wupintype=null;}else{ spNumber+=wupintype; }
	if(gamelevel == null) gamelevel = "全部";
	
	       ArrayList<String []> huoBiTypeList=null;
	       ArrayList<String []> daojuList=null;
              GameChongZhiManagerImpl gameChongZhiManager=GameChongZhiManagerImpl.getInstance();
              huoBiTypeList=gameChongZhiManager.getCurrencyType();
              daojuList=gameChongZhiManager.getReasontypeType();
             
              String gamelevel_search=null;
              if(!"全部".equals(gamelevel)){gamelevel_search=gamelevel; spNumber+=gamelevel; }
              
		      List goumaiList = gameChongZhiManager.getChongZhiStat_reasontype(startDay,endDay,fenqu,gamelevel_search,huoBiType,wupintype,"0",null);
		      List goumaiList_= gameChongZhiManager.getChongZhiStat_reasontype(startDay,endDay,fenqu,gamelevel_search,huoBiType,wupintype,"1",null);
	
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
              
              String sheet = "货币统计";
              jxl.write.WritableSheet ws = wwb.createSheet(sheet, 0);
              jxl.write.Label labelC = new jxl.write.Label(0, 0, "货币类型");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(1, 0, "获得数量");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(2, 0, "消耗数量");
              ws.addCell(labelC);
             
					  for(int i = 0 ; i < daojuList.size() ; i++){
                       String[] _huoBiType = daojuList.get(i);
                       labelC = new jxl.write.Label(0, i+1, _huoBiType[1]);
                       ws.addCell(labelC);
                       for(int t=0;t<goumaiList.size();t++){
                       String[] goumai=(String[])goumaiList.get(t);
                      
                       if(goumai[0].equals(_huoBiType)){
                         labelC = new jxl.write.Label(1, i+1,goumai[1]);
                         ws.addCell(labelC);
                        }
                       }
                       for(int t=0;t<goumaiList_.size();t++){
                       String[] goumai_=(String[])goumaiList_.get(t);
                       if(goumai_[0].equals(_huoBiType)){
                        labelC = new jxl.write.Label(2, i+1, goumai_[1]);
                        ws.addCell(labelC);
                       }
                       }
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