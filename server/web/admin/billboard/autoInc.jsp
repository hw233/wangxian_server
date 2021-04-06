
<%@page import="com.fy.engineserver.newBillboard.Billboard"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardStatDateManager"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardStatDate"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.fy.engineserver.util.StringTool"%><%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%!public static String modify(String webRoot) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String res = "["+sdf.format(new Date())+"]";
		
		String logFilePath = webRoot + "/billboardsManager.log";
		String commandFilePath = webRoot + "/accLog.sh";
		String command = "";
		Process process = null;
		BufferedReader br = null;
		FileOutputStream fos  = null;
		HashMap<Long,Integer> resultMap = new HashMap<Long,Integer>();
		StringBuffer sb = new StringBuffer();
		try {
			
			File commamdFile = new File(commandFilePath);
			if (!commamdFile.exists()) {//命令文件不存在，先创建文件
				if (!commamdFile.getParentFile().exists()) {
					res += "创建命令文件失败，父路径不存在:" + commamdFile.getParentFile().getAbsolutePath();
					System.out.println(res);
					return res;
				}
				
				command = " /bin/cat " + logFilePath;
				commamdFile.createNewFile();
				fos = new FileOutputStream(commamdFile);
				fos.write(command.getBytes("UTF-8"));
				fos.flush();
				System.out.println("创建shell成功:[" + commamdFile.getAbsolutePath() + "] [内容:" + command + "]");
			}
			
			File logFile = new File(logFilePath);
			process = Runtime.getRuntime().exec("/bin/sh " + commandFilePath);
			System.out.println("process.getErrorStream().available():" + process.getErrorStream().available());
			if (process.getErrorStream().available() > 0) {
				res += "执行脚本出错";
				System.out.println(res);
				return res;
			}
			System.out.println("process.getInputStream().available():" + process.getInputStream().available());
			br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			String line = null;
			while ((line = br.readLine())  != null) {
				
				if(line.contains("设置当日送糖成功")){
					String[] str = line.split(" ");
					String keyS = str[7]; 
					//[{username:lwm659197925}{id:1100000000000011796}{name:旺兔睡、}{level:150}{channel:UC_MIESHI}]
					String[] keys2 = keyS.split(":");
					String[] keys3 = keys2[2].split("}");
					String key = keys3[0];
					
					String values = str[8]; 
					//[1998]
					
					String[] values1 = values.split("\\]");
					String[] values2 = values1[0].split("\\[");
					String value = values2[1];
					
					if(resultMap.get(Long.parseLong(key)) == null){
						resultMap.put(Long.parseLong(key),Integer.parseInt(value));
					}else{
						resultMap.put(Long.parseLong(key),resultMap.get(Long.parseLong(key))+Integer.parseInt(value));
					}
				}
			}
			int count = 0;
			for(Map.Entry<Long,Integer> en : resultMap.entrySet()){
				try{
					count++;
					sb.append("id"+en.getKey()+"value"+en.getValue()+"("+count+")<br/>");
					BillboardStatDate data = BillboardStatDateManager.getInstance().getBillboardStatDate(en.getKey());
					data.setDayFlowersNum(en.getValue());
					data.setDaySweetsNum(en.getValue());
				}catch(Exception ex){
					System.out.println("执行异常某一个:" +en.getKey()+ StringUtil.getStackTrace(ex));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("执行异常:" + StringUtil.getStackTrace(e));
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("执行异常:" + e.getMessage());
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("执行异常:" + e.getMessage());
				}
			}
			if (process != null) {
				process.destroy();
			}
		}
		res += sb.toString();
		return res;
	}%>
	
	<%! 
	int count = 0;

	List<Long> list = new ArrayList<Long>();
	
	int countNum = 60;
	long 间隔 = 5*60*1000;
	long 一天 = 24*60*60*1000;
	
	Thread thread = null;
	long lastTime = 0l;
	public static boolean hasRunning = false;
	String contextPath = null;
%>
	
	
	<%
	String action = request.getParameter("action");
	contextPath = request.getRealPath("/").replace("webapps","log");
	if(action != null && action.equals("start")){
		thread = new Thread(new Runnable(){
			
			public void run(){
				hasRunning =  true;
				while(hasRunning && count < countNum){
					
					try{
						Thread.sleep(30000);
					}catch(Exception e){
						e.printStackTrace();
					}
					if(hasRunning == false)
						break;
					
					try{
						
						long now = SystemTime.currentTimeMillis();
						if(now - lastTime > 间隔){
							lastTime = now;
							count++;
							modify(contextPath);
							
							Billboard bb1 = BillboardsManager.getInstance().getBillboard("魅力","当日鲜花");
							bb1.setLastUpdateTime(now - 一天);
							Billboard bb2 = BillboardsManager.getInstance().getBillboard("魅力","当日糖果");
							bb2.setLastUpdateTime(now - 一天);
							
							BillboardsManager.logger.error("[后台刷新糖果] [第几次:"+count+"] [执行时间:"+(SystemTime.currentTimeMillis()-lastTime)+"]");
						}
					}catch(Exception e){
						BillboardsManager.logger.error("[后台刷新糖果]",e);
					}
				}
				hasRunning = false;
				thread = null;
			}
		});
	
		thread.setName("后台修改糖果");
		thread.start();
	}else if(action != null && action.equals("stop")){
		hasRunning = false;
		thread.interrupt();
	}	
	%>
<%
	if(thread == null){
		out.println("线程还没有启动，<a href='./autoInc.jsp?action=start'>点击这里</a>启动线程! <a href='./autoInc.jsp'>点击这里</a>刷新");
	}else{
		out.println("线程真正运行，已执行"+count+"次循环，<a href='./autoInc.jsp?action=stop'>点击这里</a>停止线程! <a href='./autoInc.jsp'>点击这里</a>刷新");
	}
%>	
	
	