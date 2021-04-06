package com.xuanzhi.tools.monitor;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.*;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

public class MonitorManager implements Runnable{

	public static void main(String args[]) throws Exception{
		long startTime = System.currentTimeMillis();
		int port = 8088;
		String serviceStr = null;
		
		for(int i = 0 ; i < args.length ; i++){
			if(args[i].equals("-port") && i < args.length -1){
				port = Integer.parseInt(args[i+1]);
				i++;
			}else if(args[i].equals("-service") && i < args.length -1){
				serviceStr = args[i+1];
				i++;
			}else if(args[i].equals("-h")){
				printHelp();
				System.exit(0);
			}
		}
		if(serviceStr == null){
			printHelp();
			System.exit(0);
		}
		
		String ss[] = serviceStr.split(",");
		Class cls[] = new Class[ss.length];
		for(int i = 0 ; i < ss.length ; i++){
			Class cl = Class.forName(ss[i]);
			if(MonitorService.class.isAssignableFrom(cl) == false){
				throw new Exception("给定的类["+ss[i]+"]没有实现接口"+MonitorService.class.getName());
			}
			try{
				cl.getConstructor(new Class[0]);
			}catch(Exception e){
				throw new Exception("给定的类["+ss[i]+"]没有公共的无参数的构造函数");
			}
			cls[i] = cl;
		}
		
		MonitorManager mm = new MonitorManager(cls);
		mm.port = port;
		try{
			mm.start();
			
			System.out.println(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss") + " Monitor Server Started Success in ["+(System.currentTimeMillis() - startTime)+"] ms");
		}catch(Exception e){
			System.out.println(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss") + " Monitor Server Started Failed in ["+(System.currentTimeMillis() - startTime)+"] ms");
			e.printStackTrace();
			System.exit(1);
			
		}
		
	}
	
	public static void printHelp(){
		System.out.println("[Usage] java -cp . com.xuanzhi.tools.monitor.MonitorManager [-port port] <-service serciceClassName[,serciceClassName]> [-h]");
		System.out.println("Author: yugang wang");
		System.out.println("Options:");
		System.out.println("          -port port HttpServer的端口号，只支持/status和/info?id=xx请求");
		System.out.println("          -service service 监控服务类，必须是MonitorService的实现类，多个以逗号分隔");
		System.out.println("          -h  打印此帮助");
	}
	
	protected Thread thread = null;
	
	protected int port = 8088;
	
	protected ServerSocket serverSocket ;
	
	protected Date startTime = new Date();
	
	MonitorThread monitorThreads[] = null;
	Class [] serviceClass = null;
	public MonitorManager(Class [] serviceClass){
		this.serviceClass = serviceClass;
	}
	
	public void start() throws Exception{
		
		monitorThreads = new MonitorThread[serviceClass.length];
		
		for(int i = 0 ; i < serviceClass.length ; i++){
			MonitorService service = (MonitorService)serviceClass[i].newInstance();
			monitorThreads[i] = new MonitorThread(service);
			monitorThreads[i].start();
		}
		
		serverSocket = new ServerSocket(port);
		thread = new Thread(this,"Server-Thread");
		thread.start();

	}
	
	public String formatHtmlPage(){
		StringBuffer sb = new StringBuffer();
		sb.append("<html><head><title>监控展示页面</title><meta http-equiv=\"Cache-Control\" content=\"max-age=0\">\n<meta http-equiv=\"Refresh\" content=\"15\">\n</head><body><center>\n");
		sb.append("<h2>各监控点的情况</h2><br>\n");
		sb.append("启动时间："+startTime+"&nbsp;<br><br>\n");
		sb.append("<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>\n");
		sb.append("<tr bgcolor='#00FFFF' align='center'><td>名称</td><td>最近监控时间</td>");
		int n = 16;
		for(int j = 0 ; j < n ; j++){
			sb.append("<td>监控点</td>");
		}
		sb.append("<td>图表</td><td>检测</td><tr>\n");
		
		for(int i = 0 ; i < monitorThreads.length ; i++){
			MonitorPoint mps[] = monitorThreads[i].points.toArray(new MonitorPoint[0]);
			String name = monitorThreads[i].service.getName();
			sb.append("<tr bgcolor='#FFFFFF' align='center'><td><a href='./info?id="+i+"' target='_blank'>"+name+"</a></td>");
			if(mps.length > 0){
				sb.append("<td>"+DateUtil.formatDate(new Date(mps[0].checkTime),"yyyy-MM-dd HH:mm:ss")+"</td>");
			}else{
				sb.append("<td>--</td>");
			}
			
			for(int j = 0 ; j < n ; j++){
				if(j < mps.length){
					if(mps[j].isAlerted())
						sb.append("<td bgcolor='#888888' title='"+DateUtil.formatDate(new Date(mps[j].checkTime),"yyyy-MM-dd HH:mm:ss")+"'>"+mps[j].getLevelAsString()+"</td>");
					else
						sb.append("<td title='"+DateUtil.formatDate(new Date(mps[j].checkTime),"yyyy-MM-dd HH:mm:ss")+"'>"+mps[j].getLevelAsString()+"</td>");
				}else{
					sb.append("<td>--</td>");
				}
					
			}
			if(monitorThreads[i].service.getChartSeriesPropertyName() != null)
				sb.append("<td><a href='./chart?id="+i+"' target='_blank'>查看</a></td>\n");
			else
				sb.append("<td>无图表</td>\n");
			
			//todo
			if(monitorThreads[i].checkFlag.isChecking())
				sb.append("<td><a href='./cf?id="+i+"' target='_blank'>"+monitorThreads[i].checkFlag.administrator+"</a></td></tr>\n");
			else
				sb.append("<td><a href='./cf?id="+i+"' target='_blank'>无</a></td></tr>\n");
		}
		
		sb.append("</table>\n");
		sb.append("</center></body>\n");
		sb.append("</html>\n");
		return sb.toString();
	}
	
	public String formatThreadPage(int tid){
		return showThreads(false,tid);
	}
	
	public byte[] formatServiceChart(int k) throws Exception{
		if(k >= 0 && k < monitorThreads.length){
			BufferedImage bi = GenerateChart.generate(monitorThreads[k].service,monitorThreads[k].points);
			if(bi != null){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ImageIO.write(bi,"png",out);
				
				File file = new File("/usr/local/resin/webapps/stat/aaa.png");
				
				ImageIO.write(bi,"png",file);
				
				return out.toByteArray();
			}else{
				return new byte[0];
			}
		}else{
			return new byte[0];
		}
	}
	
	public String formatServicePage(int k){
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<html><head><title>监控展示页面</title></head><body><center>\n");
		if(k >= 0 && k < monitorThreads.length){
			MonitorService service = monitorThreads[k].service;
			String address = service.getDataUrl().toString();
			if(address.length() > 60){
				address = StringUtil.replace(address,"?","? ");
				address = StringUtil.replace(address,"&","& ");
			}
			sb.append("<h2>监控服务配置</h2><br>\n");
			sb.append("<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>\n");
			sb.append("<tr align='center'><td bgcolor='#00FFFF'>名称</td><td bgcolor='#FFFFFF'>"+service.getName()+"</td></tr>\n");
			sb.append("<tr align='center'><td bgcolor='#00FFFF'>监控地址</td><td bgcolor='#FFFFFF'>"+address+"</td></tr>\n");
			sb.append("<tr align='center'><td bgcolor='#00FFFF'>监控的时间间隔</td><td bgcolor='#FFFFFF'>"+(service.getMonitorInterval()/1000)+"秒</td></tr>\n");
			sb.append("<tr align='center'><td bgcolor='#00FFFF'>报警的时间间隔</td><td bgcolor='#FFFFFF'>"+(service.getAlertInterval()/1000)+"秒</td></tr>\n");
			sb.append("<tr align='center'><td bgcolor='#00FFFF'>系统报警级别</td><td bgcolor='#FFFFFF'>"+service.getMinAlertLevel()+"级报警</td></tr>\n");
			sb.append("<tr align='center'><td bgcolor='#00FFFF'>恢复通知级别</td><td bgcolor='#FFFFFF'>"+service.getMaxNotifyLevel()+"级报警</td></tr>\n");
			sb.append("<tr align='center'><td bgcolor='#00FFFF'>简单描述</td><td bgcolor='#FFFFFF'>"+service.getDescription()+"级报警</td></tr>\n");
			sb.append("</table>\n");
			
			sb.append("<h2>监控数据</h2><br>\n");
			sb.append("<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>\n");
			sb.append("<tr align='center'  bgcolor='#00FFFF'><td>编号</td><td>监控时间</td><td>级别</td><td>数据</td></tr>\n");
			Iterator<MonitorPoint> it = monitorThreads[k].points.iterator();
			int count=1;
			while(it.hasNext()){
				MonitorPoint p = it.next();
				String data = "HTTP_RESPONSE_CODE="+p.data.httpResponseCode+"<br>";
				Enumeration en = p.data.props.propertyNames();
				while(en.hasMoreElements()){
					String key = (String)en.nextElement();
					data += key+"="+p.data.getData(key)+"<br>";
				}
				String color = "#FFFFFF";
				if(p.isAlerted()) color="#FF0000";
				sb.append("<tr align='center'  bgcolor='"+color+"'><td>"+count+"</td><td>"+DateUtil.formatDate(new Date(p.getCheckTime()),"yyyy-MM-dd HH:mm:ss")+"</td><td>"+p.getLevelAsString()+"</td><td>"+data+"</td></tr>\n");
				count++;
			}
			sb.append("</table>\n");
		}else{
			sb.append("<h4>您需要的监控服务不存在</h4>");
		}
		
		sb.append("</center></body>\n");
		sb.append("</html>\n");
		return sb.toString();
	}
	
	protected static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1,16,60L,TimeUnit.SECONDS,new SynchronousQueue(),new ThreadPoolExecutor.CallerRunsPolicy());
	
	public class Handler implements Runnable{
		Socket s = null;
		public Handler(Socket s){
			this.s = s;
		}
		
		public void run(){
			ByteArrayOutputStream outputData = new ByteArrayOutputStream();
			
			
			byte bytes[] = new byte[1024];
			
			try{
				InputStream input = s.getInputStream();
				int n = input.read(bytes);
				
				String httpRequest = new String(bytes,0,n);
				String ss[] = httpRequest.split(" ");
				if(ss.length < 3){
					StringBuffer sb = new StringBuffer();
					sb.append("HTTP/1.0 404 bad request\n");
					sb.append("Server: MonitorManager\n");
					sb.append("Cache-Control: no-cache\n");
					sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
					sb.append("Content-Type: text/html;charset=gb2312\n");
					sb.append("\n");
					outputData.write(sb.toString().getBytes());
				}else{
					StringBuffer sb = new StringBuffer();
					if(ss[1].startsWith("/threads")){
						try{
							int tid = -1;
							if(ss[1].indexOf("thread_id=") > 0){
								String kk = ss[1].substring(ss[1].indexOf("thread_id=")+10);
								if(kk.indexOf("&") > 0){
									kk = kk.substring(0,kk.indexOf("&"));
									
								}
								try{
									tid = Integer.parseInt(kk);
								}catch(Exception e){}
							}
							String page = formatThreadPage(tid);
							sb.append("HTTP/1.0 200 OK\n");
							sb.append("Server: MonitorManager\n");
							sb.append("Cache-Control: no-cache\n");
							sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
							sb.append("Content-Type: text/html;charset=gb2312\n");
							sb.append("Content-Length: "+page.getBytes().length+"\n");
							sb.append("Date: "+new java.util.Date()+"\n");
							sb.append("\n");
							sb.append(page);
							outputData.write(sb.toString().getBytes());
						}catch(Exception e){
							sb.append("HTTP/1.0 500 server error\n");
							sb.append("Server: MonitorManager\n");
							sb.append("Cache-Control: no-cache\n");
							sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
							sb.append("Content-Type: text/html;charset=gb2312\n");
							sb.append("\n");
							ByteArrayOutputStream bout = new ByteArrayOutputStream();
							e.printStackTrace(new PrintStream(bout));
							sb.append(new String(bout.toByteArray()));
							outputData.write(sb.toString().getBytes());
						}
					}
					else if(ss[1].equals("/status")){
						try{
							String page = formatHtmlPage();
							sb.append("HTTP/1.0 200 OK\n");
							sb.append("Server: MonitorManager\n");
							sb.append("Cache-Control: no-cache\n");
							sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
							sb.append("Content-Type: text/html;charset=gb2312\n");
							sb.append("Content-Length: "+page.getBytes().length+"\n");
							sb.append("Date: "+new java.util.Date()+"\n");
							sb.append("\n");
							sb.append(page);
							outputData.write(sb.toString().getBytes());
						}catch(Exception e){
							sb.append("HTTP/1.0 500 server error\n");
							sb.append("Server: MonitorManager\n");
							sb.append("Cache-Control: no-cache\n");
							sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
							sb.append("Content-Type: text/html;charset=gb2312\n");
							sb.append("\n");
							ByteArrayOutputStream bout = new ByteArrayOutputStream();
							e.printStackTrace(new PrintStream(bout));
							sb.append(new String(bout.toByteArray()));
							outputData.write(sb.toString().getBytes());
						}
					}else if(ss[1].startsWith("/info?id=")){
						String k = ss[1].substring(9);
						try{
							int kk = Integer.parseInt(k.trim());
							String page = formatServicePage(kk);
							sb.append("HTTP/1.0 200 OK\n");
							sb.append("Server: MonitorManager\n");
							sb.append("Cache-Control: no-cache\n");
							sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
							sb.append("Content-Type: text/html;charset=gb2312\n");
							sb.append("Content-Length: "+page.getBytes().length+"\n");
							sb.append("Date: "+new java.util.Date()+"\n");
							sb.append("\n");
							sb.append(page);
							outputData.write(sb.toString().getBytes());
						}catch(Exception e){
							sb.append("HTTP/1.0 500 server error\n");
							sb.append("Server: MonitorManager\n");
							sb.append("Cache-Control: no-cache\n");
							sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
							sb.append("Content-Type: text/html;charset=gb2312\n");
							sb.append("\n");
							ByteArrayOutputStream bout = new ByteArrayOutputStream();
							e.printStackTrace(new PrintStream(bout));
							sb.append(new String(bout.toByteArray()));
							outputData.write(sb.toString().getBytes());
						}
					}else if(ss[1].startsWith("/chart?id=")){
						String k = ss[1].substring(10);
						try{
							int kk = Integer.parseInt(k.trim());
							sb.append("HTTP/1.0 200 OK\n");
							sb.append("Server: MonitorManager\n");
							sb.append("Cache-Control: no-cache\n");
							sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
							sb.append("Content-Type: text/html;charset=gb2312\n");
							sb.append("Date: "+new java.util.Date()+"\n");
							sb.append("\n");
							sb.append("<html><head><meta http-equiv=\"Cache-Control\" content=\"max-age=0\">\n<meta http-equiv=\"Refresh\" content=\"15\">\n</head>\n");
							if(kk >= 0 && kk < monitorThreads.length){
							sb.append("<body><center><h3>"+monitorThreads[kk].service.getName()+"图表(自动刷新)</h3><img src='./img?id="+kk+"'/></center></body></html>");
							}else{
								sb.append("<body><center><h3>非法的输入参数</h3><img src='./img?id="+kk+"'/></center></body></html>");
							}
							outputData.write(sb.toString().getBytes());
						}catch(Exception e){
							sb.append("HTTP/1.0 500 server error\n");
							sb.append("Server: MonitorManager\n");
							sb.append("Cache-Control: no-cache\n");
							sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
							sb.append("Content-Type: text/html;charset=gb2312\n");
							sb.append("\n");
							ByteArrayOutputStream bout = new ByteArrayOutputStream();
							e.printStackTrace(new PrintStream(bout));
							sb.append(new String(bout.toByteArray()));
							outputData.write(sb.toString().getBytes());
						}
					}else if(ss[1].startsWith("/img?id=")){
						String k = ss[1].substring(8);
						try{
							int kk = Integer.parseInt(k.trim());
							bytes = formatServiceChart(kk);
							sb.append("HTTP/1.0 200 OK\n");
							sb.append("Server: MonitorManager\n");
							sb.append("Cache-Control: no-cache\n");
							sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
							sb.append("Content-Type: image/png\n");
							sb.append("Content-Length: "+bytes.length+"\n");
							sb.append("Date: "+new java.util.Date()+"\n");
							sb.append("\n");
							outputData.write(sb.toString().getBytes());
							outputData.write(bytes);
						}catch(Exception e){
							sb.append("HTTP/1.0 500 server error\n");
							sb.append("Server: MonitorManager\n");
							sb.append("Cache-Control: no-cache\n");
							sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
							sb.append("Content-Type: text/html;charset=gb2312\n");
							sb.append("\n");
							ByteArrayOutputStream bout = new ByteArrayOutputStream();
							e.printStackTrace(new PrintStream(bout));
							sb.append(new String(bout.toByteArray()));
							outputData.write(sb.toString().getBytes());
						}
					}else if(ss[1].startsWith("/cf?")){
						String k = ss[1].substring(4);
						try{
							HashMap<String,String> paramters = parseQueryString(k);
							int kk = Integer.parseInt(paramters.get("id"));
							boolean submitted = String.valueOf("true").equals(paramters.get("submitted"));
							String info = "";
							String admin = "";
							long time = 0;
							if(submitted){
								info = paramters.get("info");
								admin = paramters.get("admin");
								time = Integer.parseInt(paramters.get("period"));
								if(time > 0)
									monitorThreads[kk].setChecking(admin,info,time*1000);
							}else if(kk>= 0 && kk < monitorThreads.length){
								info = monitorThreads[kk].checkFlag.information;
								admin = monitorThreads[kk].checkFlag.administrator;
								time = (monitorThreads[kk].checkFlag.endTime - System.currentTimeMillis())/1000;
								if(time < 0) time = 0;
							}
							sb.append("HTTP/1.0 200 OK\n");
							sb.append("Server: MonitorManager\n");
							sb.append("Cache-Control: no-cache\n");
							sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
							sb.append("Content-Type: text/html;charset=gb2312\n");
							sb.append("Date: "+new java.util.Date()+"\n");
							sb.append("\n");
							sb.append("<html><head><meta http-equiv=\"Cache-Control\" content=\"max-age=0\">\n</head>\n");
							if(kk >= 0 && kk < monitorThreads.length){
								sb.append("<body><center><h3>"+monitorThreads[kk].service.getName()+"管理员检测设置</h3>");
								sb.append("<table border='0' cellpadding='0' cellspacing='0' bgcolor='#FFFFFF' align='center'>");
								sb.append("<form id='myf' action='./cf'>\n");
								sb.append("<input type='hidden' name='id' id='id' value='"+kk+"'>");
								sb.append("<input type='hidden' name='submitted' id='submitted' value='true'>");
								sb.append("<tr><td align='right'>请输入您的名字：</td><td align='left'><input type='text' name='admin' id='admin' size='15' value='"+admin+"'></td><td align='left'>您的名字，用于通知其他运营人员</td></tr>");
								sb.append("<tr><td align='right'>请输入手动检测时长：</td><td align='left'><input type='text' name='period' size='15' id='period' value='"+time+"'></td><td align='left'>单位是秒，比如3600，就是说3600秒内系统不会再发报警</td></tr>");
								sb.append("<tr><td align='right'>请输入相关描述：</td><td align='left'><input type='text' name='info' id='info' size='40' value='"+info+"'></td><td align='left'>此描述将发送给其他运营人员</td></tr>");
								sb.append("<tr><td>&nsbp;</td><td align='left' ><input type='submit' value='提  交'></td><td>&nsbp;</td></tr>");
								sb.append("</form>\n");
								sb.append("</table>");
								sb.append("</center></body></html>");
							}else{
								sb.append("<body><center><h3>非法的输入参数</h3></center></body></html>");
							}
							outputData.write(sb.toString().getBytes());
						}catch(Exception e){
							sb.append("HTTP/1.0 500 server error\n");
							sb.append("Server: MonitorManager\n");
							sb.append("Cache-Control: no-cache\n");
							sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
							sb.append("Content-Type: text/html;charset=gb2312\n");
							sb.append("\n");
							ByteArrayOutputStream bout = new ByteArrayOutputStream();
							e.printStackTrace(new PrintStream(bout));
							sb.append(new String(bout.toByteArray()));
							outputData.write(sb.toString().getBytes());
						}
					}else if(ss[1].equals("/shutdown")){
						sb.append("HTTP/1.0 200 OK\n");
						sb.append("Server: MonitorManager\n");
						sb.append("Cache-Control: no-cache\n");
						sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
						sb.append("Content-Type: text/html;charset=gb2312\n");
						sb.append("\n");
						sb.append("MonitorManager ShutDown OK");
						System.out.println(""+DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ["+Thread.currentThread().getName()+"] [Monitor-ShutDown] ["+s+"]");
						running = false;
						serverSocket.close();
						thread.interrupt();
						outputData.write(sb.toString().getBytes());
					}else{
						sb.append("HTTP/1.0 404 bad request\n");
						sb.append("Server: MonitorManager\n");
						sb.append("Cache-Control: no-cache\n");
						sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
						sb.append("Content-Type: text/html;charset=gb2312\n");
						sb.append("\n");
						outputData.write(sb.toString().getBytes());
					}
				}
				
			}catch(Exception e){
				StringBuffer sb = new StringBuffer();
				sb.append("HTTP/1.0 500 server error\n");
				sb.append("Server: MonitorManager\n");
				sb.append("Cache-Control: no-cache\n");
				sb.append("Expires: Thu, 01 Dec 1994 16:00:00 GMT\n");
				sb.append("Content-Type: text/html;charset=gb2312\n");
				sb.append("\n");
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(bout));
				sb.append(new String(bout.toByteArray()));
				
				try {
					outputData.write(sb.toString().getBytes());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}finally{
				try{
					OutputStream out = s.getOutputStream();
					out.write(outputData.toByteArray());
					out.flush();
					s.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public static HashMap<String,String> parseQueryString(String queryString){
		try {
			queryString = java.net.URLDecoder.decode(queryString,System.getProperty("file.encoding"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String pairs[] = StringUtil.split(queryString,'&');
		HashMap<String,String> map = new HashMap<String,String> ();
		for(int i = 0 ; i < pairs.length ; i++){
			String cs[] = StringUtil.split(pairs[i],'=');
			if(cs.length == 2){
				map.put(cs[0],cs[1]);
			}
		}
		return map;
	}
	boolean running = true;
	
	public void run() {
		
		while(running){
			Socket s = null;
			try{
				 s = serverSocket.accept();
				 if(s != null){
					 Handler h = new Handler(s);
					 threadPool.execute(h);
				 }
			}catch(Exception e){
				if(!running) break;
				e.printStackTrace(System.out);
				
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss") + " Monitor Server Stop!");
		System.exit(0);
	}

	protected static ThreadMXBean oMBean = null;
	static{
		oMBean = ManagementFactory.getThreadMXBean();
	}
	String getThreadGroupInfo(ThreadGroup group, boolean bDump, long lThreadID)
	{
		StringBuffer rc = new StringBuffer();
		rc.append("<BR><B>");
		rc.append("Thread Group: " + group.getName());
		rc.append("</B> : ");
		rc.append("max priority:" + group.getMaxPriority() + ", demon:" + group.isDaemon() + ", threads:" + group.activeCount());
		rc.append("<blockquote>");
		Thread threads[] = new Thread[group.activeCount()];
		group.enumerate(threads, false);
		for (int i = 0; i < threads.length && threads[i] != null; i++)
		{
			ThreadInfo oInfo = oMBean.getThreadInfo(threads[i].getId());
			rc.append("<B>");
			rc.append("Thread(" + threads[i].getId() + "): " + threads[i].getName());
			rc.append(" : ");
			rc.append("<font color=\"blue\">" + oInfo.getThreadState() + "</font></B>");
			rc.append("<BR>");
			if (bDump || threads[i].getId() == lThreadID)
			{
				rc.append("BlockedTime:"+oInfo.getBlockedTime() + ", BlockedCount:" + oInfo.getBlockedCount() + ", WaitedTime:" + oInfo.getWaitedTime() + ", WaitedCount:" + oInfo.getWaitedCount()).append("<BR>");
				StackTraceElement[] oTraceArr = threads[i].getStackTrace();
				int iLength = oTraceArr == null ? 0 : oTraceArr.length;
				for (int j = 0; j < iLength; j++)
				{
					rc.append("at:" + oTraceArr[j]);
					rc.append("<BR>");
				}
			}
		}
		ThreadGroup groups[] = new ThreadGroup[group.activeGroupCount()];
		group.enumerate(groups, false);
		for (int i = 0; i < groups.length && groups[i] != null; i++)
		{
			rc.append(getThreadGroupInfo(groups[i], bDump, lThreadID));
		}
		rc.append("</blockquote>");
		return rc.toString();
	}
	
	public String showThreads(boolean bDump, long lThreadID)
	{
	// Get the root thread group
		ThreadGroup root = Thread.currentThread().getThreadGroup();
		while (root.getParent() != null)
		{
			root = root.getParent();
		}
		// I'm not sure why what gets reported is off by +1, 
		// but I'm adjusting so that it is consistent with the display
		int activeThreads = root.activeCount()-1;
		// I'm not sure why what gets reported is off by -1
		// but I'm adjusting so that it is consistent with the display
		int activeGroups = root.activeGroupCount()+1;
		String rc = "<b>Peak Threads:</b> "+oMBean.getPeakThreadCount()+"<br>"+"<b>Total Threads:</b> "+activeThreads+"<br>"+"<b>Total Thread Groups:</b> "+activeGroups+"<br>"+getThreadGroupInfo(root, bDump, lThreadID);
		return rc;
	}
	
}
