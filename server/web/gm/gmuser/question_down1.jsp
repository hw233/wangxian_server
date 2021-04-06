<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.xuanzhi.boss.game.model.GamePlayer"%>
<%@page import="org.apache.poi.hssf.usermodel.*"%>
<%@include file="../authority.jsp" %>
<%@include file="../header.jsp"%>
<%
	try {
		GmMailReplay gmreplay = GmMailReplay.getInstance();
		PlayerManager pmanager = PlayerManager.getInstance();
		MailManager mmanager = MailManager.getInstance();
		MailRecordManager mrmanager = MailRecordManager.getInstance();
		ArticleManager acmanager = ArticleManager.getInstance();
		ActionManager amanager = ActionManager.getInstance();
		String username = session.getAttribute("username").toString();
		String action = request.getParameter("action");
		String gmName = session.getAttribute("gmid").toString();//判断是否登录
		String pid = request.getParameter("pid");//获取过滤参数
		String mcontent = request.getParameter("mcontent");
		String stime = request.getParameter("stime");
		String etime = request.getParameter("etime");
		String gname = request.getParameter("gname");
		List<Mail> mails = new ArrayList<Mail>();
		if ((pid == null || "".equals(pid.trim()))
				&& (mcontent == null || "".equals(mcontent.trim()))&&(stime==null||etime==null))//如果没有过滤过程则获取固定长度的邮件
			mails = gmreplay.getAllGmMail(gname);
		if (pid != null && !"".equals(pid.trim())) {//如果过滤参数不为空则获取过滤邮件
			// paname = URLDecoder.decode(paname);
			List<Mail> ms = gmreplay.getAllGmMail(gname);//获取所有的GM..邮件
			mails = new ArrayList<Mail>();
			if(ms!=null&&ms.size()>0){
			for (Mail m : ms) {//如果邮件的发件人和过滤人相同，则获取！
				try {
					if (pid.trim().equals(m.getPoster()+""))
						mails.add(m);
				} catch (Exception e) {
					continue;
				}
			}
           }
		}
		if (mcontent != null && !"".equals(mcontent.trim())) {//如果过滤参数不为空则获取过滤邮件
			out.print(mcontent);
			// paname = URLDecoder.decode(paname);
			List<Mail> ms = gmreplay.getAllGmMail(gname);//获取所有的gname邮件
			mails = new ArrayList<Mail>();
			for (Mail m : ms) {
				try {
					if (m.getContent().contains(mcontent))
						mails.add(m);
				} catch (Exception e) {
					continue;
				}
			}

		}
		if(stime!=null&&etime!=null){
				 Date sdate = DateUtil.parseDate(stime,"yyyy-MM-dd HH:mm");
				 Date edate = DateUtil.parseDate(etime,"yyyy-MM-dd HH:mm");
				  mails = gmreplay.getBetweenMail("GM01",sdate,edate);
				
				}
		out.print(mails.size());
		//StringBuffer sb = new StringBuffer();
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("questionmail");
		HSSFRow row = sheet.createRow((short) 0);
		//sheet 创建一行
		HSSFCell cell1 = row.createCell((short) 0);
		HSSFCell cell2 = row.createCell((short) 1);
		HSSFCell cell3 = row.createCell((short) 2);
		HSSFCell cell4 = row.createCell((short) 3);
		HSSFCell cell5 = row.createCell((short) 4);
		cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell2.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell3.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell4.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell5.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell3.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell4.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell5.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell1.setCellValue("【账户名】：");
		cell2.setCellValue("【角色名】:");
		cell3.setCellValue("【发送时间】");
		cell4.setCellValue("【邮件标题】：");
		cell5.setCellValue("【邮件标题】：");

		int i = 1;
		for (Mail m : mails) {
			HSSFRow row1 = sheet.createRow((short) (i++));
			HSSFCell cel1 = row1.createCell((short) 0);
			HSSFCell cel2 = row1.createCell((short) 1);
			HSSFCell cel3 = row1.createCell((short) 2);
			HSSFCell cel4 = row1.createCell((short) 3);
			HSSFCell cel5 = row1.createCell((short) 4);
			cel1.setCellType(HSSFCell.CELL_TYPE_STRING);
			cel2.setEncoding(HSSFCell.ENCODING_UTF_16);
			cel3.setEncoding(HSSFCell.ENCODING_UTF_16);
			cel4.setEncoding(HSSFCell.ENCODING_UTF_16);
			cel5.setEncoding(HSSFCell.ENCODING_UTF_16);
			cel1.setEncoding(HSSFCell.ENCODING_UTF_16);
			cel2.setCellType(HSSFCell.CELL_TYPE_STRING);
			cel3.setCellType(HSSFCell.CELL_TYPE_STRING);
			cel4.setCellType(HSSFCell.CELL_TYPE_STRING);
			cel5.setCellType(HSSFCell.CELL_TYPE_STRING);
			try {
				cel1.setCellValue(pmanager.getPlayer(m.getPoster())
						.getUsername());
				cel2.setCellValue(pmanager.getPlayer(m.getPoster())
						.getName());
				cel3.setCellValue(DateUtil.formatDate(
						m.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
				cel4.setCellValue(m.getTitle());
				cel5.setCellValue(m.getContent());
			} catch (Exception e) {
				cel1.setCellValue("用户名不存在" + m.getPoster());
				cel2.setCellValue("角色名不存在" + m.getPoster());
				cel3.setCellValue(DateUtil.formatDate(
						m.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
				cel4.setCellValue(m.getTitle());
				cel5.setCellValue(m.getContent());
				continue;
			}
		}
		response.reset();//可以加也可以不加  
		response.setContentType("application/vnd.ms-Excel");

		//application.getRealPath("/main/mvplayer/CapSetup.msi");获取的物理路径   
		String filedisplay = "questionmail.xls";
		filedisplay = URLEncoder.encode(filedisplay, "utf-8");
		response.addHeader("Content-Disposition",
				"attachment;filename="
						+ new String(filedisplay.getBytes("utf-8"),
								"gbk"));

		try {
			java.io.OutputStream outp = null;
			outp = response.getOutputStream();
			wb.write(outp);
			//    
			outp.flush();
			// 要加以下两句话，否则会报错  
			//java.lang.IllegalStateException: getOutputStream() has already been called for //this response    
			//out.clear();
			out = pageContext.pushBody();

			//java.lang.IllegalStateException: getOutputStream() has already been called for //this response    
			//out.clear();
			out = pageContext.pushBody();
		} catch (Exception e) {
		    out.println(StringUtil.getStackTrace(e));
			//System.out.println("Error!");
			//e.printStackTrace();
		} finally {

			// 这里不能关闭    
			//if(outp != null)  
			//{  
			//outp.close();  
			//outp = null;  
			//}  
		}

	} catch (Exception e) {
	    out.println(StringUtil.getStackTrace(e));
		//e.printStackTrace();
		//RequestDispatcher rdp = request
		//		.getRequestDispatcher("visitfobiden.jsp");
		//rdp.forward(request, response);

	}
%>
