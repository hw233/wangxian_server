<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*,java.io.*,
,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.simplejpa.impl.*,com.xuanzhi.tools.page.PageUtil,
java.lang.reflect.*,com.fy.engineserver.newtask.*,com.fy.engineserver.newtask.service.*,
com.fy.engineserver.jiazu.service.*,com.fy.engineserver.jiazu.*
"%><%! 

%><%
	
	JiazuManager jm = JiazuManager.getInstance();
	

	WordFilter wf = WordFilter.getInstance();
	String fileName = wf.getFilterFile();
	File file = new File(fileName);
	BufferedReader br = new BufferedReader(new FileReader(file));
	String line = null;
	int count = 0;
	int linenum = 0;
	ArrayList<String> al = new ArrayList<String>();
	while((line = br.readLine()) != null) 
	{
		linenum++;
		line = line.trim().toLowerCase();
		String str[] = line.split("\\t");
		if(str.length == 2){
			al.add(str[0]);
		}
	}
	boolean modify = false;
	String action = request.getParameter("action");
	if(action != null && action.equals("delete")){
		int index = Integer.parseInt(request.getParameter("index"));
		al.remove(index);
		modify = true;
	}else if(action != null && action.equals("add")){
		String word = request.getParameter("word");
		String ss[] = word.split(",");
		for(int i = 0 ; i < ss.length ; i++){
			if(ss[i].trim().length() > 0){
				al.add(ss[i].trim());
				modify = true;
			}
		}
	}else if(action != null && action.equals("fa_gong_zi")){
		Map<Long,Jiazu> jiazuLruCacheByID = jm.getJiazuLruCacheByID();

		for (Iterator<Long> itor = jiazuLruCacheByID.keySet().iterator(); itor.hasNext();) {
			Jiazu jiazu = jiazuLruCacheByID.get(itor.next());
			if (jiazu != null) {
				jiazu.calculateSalary(System.currentTimeMillis());
			}
		}
	}else if(action != null && action.equals("set_time")){
		Map<Long,Jiazu> jiazuLruCacheByID = jm.getJiazuLruCacheByID();

		for (Iterator<Long> itor = jiazuLruCacheByID.keySet().iterator(); itor.hasNext();) {
			Jiazu jiazu = jiazuLruCacheByID.get(itor.next());
			if (jiazu != null) {
				jiazu.setLastCalculateSalaryTime(System.currentTimeMillis() + 48 * 3600 * 1000);
			}
		}
	}
	
	if(modify){
		BufferedWriter wr = new BufferedWriter(new FileWriter(file));
		for(String s : al){
			wr.write(s+"\t0\n");
		}
		wr.close();
	}
	
%>
<html><head>
</HEAD>
<BODY>
<h2>修复家族工资</h2>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>家族</td>
<td>上次发工资时间</td>
<td>上次发工资周索引</td>
<td>现在的周索引</td>
</tr>
<%
	Map<Long,Jiazu> jiazuLruCacheByID = jm.getJiazuLruCacheByID();

	for (Iterator<Long> itor = jiazuLruCacheByID.keySet().iterator(); itor.hasNext();) {
	Jiazu jiazu = jiazuLruCacheByID.get(itor.next());
	if (jiazu != null) {
		Calendar lastCalculateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT-8"));
		lastCalculateTime.setTimeInMillis(jiazu.getLastCalculateSalaryTime());
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT-8"));
		now.setTimeInMillis(System.currentTimeMillis() + 6 * 24 * 3600*1000);
		//jiazu.calculateSalary(now);
		out.println("<tr bgcolor='#FFFFFF'><td>"+jiazu.getName()+"</td><td>"+DateUtil.formatDate(new Date(jiazu.getLastCalculateSalaryTime()),"yyyy-MM-dd HH:mm:ss")+"</td><td>"+lastCalculateTime.get(Calendar.WEEK_OF_YEAR)+"</td><td>"+now.get(Calendar.WEEK_OF_YEAR)+"</td></tr>");
	}
}
%></table>
<a href='./wordfilter.jsp?action=fa_gong_zi'>发家族工资</a>  | <a href='./wordfilter.jsp?action=set_time'>设置最后一次发工资时间为当前+48小时</a>

<h2>屏蔽词列表</h2>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td>
<td>词汇</td>
<td>操作</td>
</tr>
<%
     for(int i = 0 ; i < al.size() ; i++){
    	 String s = al.get(i);
    	 out.println("<tr bgcolor='#FFFFFF' align='center'><td>"+(i+1)+"</td><td>"+s+"</td><td><a href='./wordfilter.jsp?action=delete&index="+i+"'>删除</a></td></tr>");
     } 
        
%></table>
<h2>添加屏蔽词</h2>
<form id="f2" name="f2" method="get">
<input type='hidden' name='action' value='add'>
请输入屏蔽词，多个词可以用逗号分隔：<input type='text' name='word' value='' size='50'>
<input type="submit" value="提交">
</form>
</BODY></html>
