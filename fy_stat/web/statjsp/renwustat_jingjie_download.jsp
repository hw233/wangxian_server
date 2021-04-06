<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,jxl.format.UnderlineStyle,
	jxl.write.WritableFont,jxl.Workbook,java.net.*"
	%>

  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String taskType=request.getParameter("taskType");
	String fenqu=request.getParameter("fenqu");
	String task=request.getParameter("task");
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
    String spNumber="jingJieRenWu"+startDay+endDay; 
	
	if("0".equals(taskType)){taskType=null;} else{spNumber+=taskType;}
	if("0".equals(fenqu)){fenqu=null;}else{spNumber+=fenqu;}
	if("0".equals(task)){task=null;}else{spNumber+=task;}
	
              TaskinfoManagerImpl taskinfoManager=TaskinfoManagerImpl.getInstance();
            //List<String[]>  taskstatList=taskinfoManager.getTaskStat(startDay,endDay,fenqu,task,taskType,null);//接受
             List<String[]>  taskstatList=taskinfoManager.getTaskStat(startDay,endDay,fenqu,task,taskType,"完成");//完成
             
             
              String renwuArray[][]={
				 {"补缺堵漏颠倒巅","15"},
				 {"退符进火入涌泉","15"},
				 {"凝神静气上灵台","15"},
				 {"三花聚顶汇泥丸","15"},
				 {"脱胎换骨似神仙","15"},
				 {"仗剑而歌斩心魔","21"},
				 {"业净六根成慧眼","21"},
				 {"楚天极目畅辽阔","35"},
				 {"天地浩然有正气","39"},
				 {"访得真君开大智","40"},
				 {"阿难入定待金丹","41"},
				 {"百病不侵度流年","46"},
				 {"普度众生出圣贤","51"},
				 {"虚室生光化紫霜","55"},
				 {"真空一境去凡尘","60"},
				 {"混元桩头铁脚仙","80"},
				 
				 {"调畅气血干沐面","80"},
				 {"阴阳交济成大统","80"},
				 {"潜移默化赤子心","80"},
				 {"霞光满室起莲台","80"},
				 {"乘物游心竞自由","80"},
				 {"纵横寰宇逍遥游","80"},
				 {"御风而行至千里","80"},
				 {"冰原寻仙不辞险","80"},
				 {"道德经里悟非凡","80"},
				 {"水火既济小周天","80"},
				 {"六神合一入毫巅","80"},
				 {"五气朝元归本尊","80"},
				 {"逆转乾坤御强敌","80"},
				 {"九天十地证无极","80"}};    
             
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
          
              String sheet = "境界任务完成统计";
              jxl.write.WritableSheet ws = wwb.createSheet(sheet, 0);
              jxl.write.Label labelC = new jxl.write.Label(0, 0, "任务名称");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(1, 0, "接受数");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(2, 0, "完成数");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(3, 0, "完成比");
              ws.addCell(labelC);
           
           	for(int j=0;j<renwuArray.length;j++){
           
           
               for(int i = 0 ; i < taskstatList.size() ; i++){
					String[] taskstat=taskstatList.get(i);
					Long bi=0L;
					
				if(taskstat[0].equals(renwuArray[j][0])){
				
				  if(taskstat[1]!=null&&!"0".equals(taskstat[1])&&taskstat[2]!=null){
				    bi=Long.parseLong(taskstat[2])*100/Long.parseLong(taskstat[1]);
				    }
				  labelC = new jxl.write.Label(0, j+1, taskstat[0]);
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(1, j+1, taskstat[1]);
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(2, j+1, taskstat[2]);
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(3, j+1, bi+" %");
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