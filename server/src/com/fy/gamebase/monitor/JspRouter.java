package com.fy.gamebase.monitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * 转发jsp请求到指定的类。
 * 省去总是要创建jsp文件，并在leftframe.jsp中添加；
 * 将jsp逻辑交由java代码处理，方便编写，提高复用率。
 * 
 * create on 2013年8月2日
 */
public class JspRouter {
	/**
	 * @param out
	 * @param request 需要指定类名 class -> com.xxx.xxx.Xxxx
	 * @throws IOException 
	 */
	public void proc(Writer out, Map<String, String[]> request) throws IOException{
		String clStr = getString(request, "class");
		if(clStr == null){
			out.append("没有指定class");
			return;
		}
		Class<?> cz = null;
		try {
			cz = (Class<?>) Class.forName(clStr);
		} catch (ClassNotFoundException e) {
			out.append("没有找到指定的类:");
			out.append(clStr);
			return;
		}
		if(!JspProc.class.isAssignableFrom(cz)){
			out.append("指定的类"+clStr+"未实现"+JspProc.class.getName()+"的接口");
			return;
		}
		JspProc inst = null;
		try {
			inst = (JspProc) cz.newInstance();
		} catch (InstantiationException e) {
			out.append("创建出错:"+e);
			appendException(out, e);
			return;
		} catch (IllegalAccessException e) {
			out.append("访问出错:"+e);
			appendException(out, e);
			return;
		}
		inst.proc(out, request);
		out.append("<br/>执行完毕");
	}

	public static void appendException(Writer out, Exception e)
			throws IOException {
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw, true);
		e.printStackTrace(w);
		w.flush();
		w.close();
		out.append(sw.toString());
	}
	
	public static String getString(Map<String, String[]> request, String key){
		String[] arr = request.get(key);
		if(arr == null){
			return null;
		}
		return arr[0].trim();
	}
}
