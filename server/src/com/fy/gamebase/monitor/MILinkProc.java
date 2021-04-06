package com.fy.gamebase.monitor;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 监视器链接处理器，当服务器监视界面的链接被点击后，该点击会被路由到对应的处理器进行处理。
 * 
 * create on 2013年7月29日
 */
public interface MILinkProc {
	public String getName();
	public void procClick(MILink link, Map<String, String[]> params, Writer out) throws IOException;
}
