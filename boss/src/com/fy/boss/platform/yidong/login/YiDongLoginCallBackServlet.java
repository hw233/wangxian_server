package com.fy.boss.platform.yidong.login;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.FileUtils;

public class YiDongLoginCallBackServlet extends HttpServlet{

	private String cacheUrl = "http://124.248.40.21:8882/game_gateway/admin/notifyCmccLoginCache.jsp";
	
	protected void service(HttpServletRequest req, HttpServletResponse res){
		String str = "1";
		try{
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");
			str = FileUtils.getContent(req.getInputStream());
			if(str == null){
				str = "2";
				BufferedInputStream bf = new BufferedInputStream(req.getInputStream());
				BufferedReader bfr = new BufferedReader(new InputStreamReader(bf));
				String line = null;
				while(( line = bfr.readLine() )!= null){
					str += line;
				}

				try{
					bfr.close();
					bf.close();
				}catch (Exception e) {
					PlatformSavingCenter.logger.error("[移动登陆回调] [关闭流时出现异常] [str:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				}
			}
			String userId = new String(req.getParameter("userId").getBytes(),"utf-8");
			String key = new String(req.getParameter("key").getBytes(),"utf-8");
			String cpId = new String(req.getParameter("cpId").getBytes(),"utf-8");
			String cpServiceId = new String(req.getParameter("cpServiceId").getBytes(),"utf-8");
			String channelId = new String(req.getParameter("channelId").getBytes(),"utf-8");
			String p = new String(req.getParameter("p").getBytes(),"utf-8");
			String region = new String(req.getParameter("region").getBytes(),"utf-8");
			String Ua = new String(req.getParameter("Ua").getBytes(),"utf-8");
			
			Passport passport = PassportManager.getInstance().getPassport("YDSDKUSER_"+userId);
			if(passport == null){
				passport = new Passport();
				passport.setUserName("YDSDKUSER_"+userId);
				passport.setPassWd(p);
				passport.setFromWhere("移动SDK");
				passport.setRegisterChannel("139NEWSDK_MIESHI");
				passport.setLastLoginChannel("139NEWSDK_MIESHI");
				passport.setLastLoginDate(new Date());
				passport = PassportManager.getInstance().createPassport(passport);
			}else{
				passport.setLastLoginDate(new Date());
				passport.setPassWd(p);
				PassportManager.getInstance().update(passport);
			}

			res.getWriter().write("0");
			PlatformSavingCenter.logger.warn("[移动登陆回调] [完成] [userId:"+userId+"] [key:"+key+"] [cpId:"+cpId+"] [cpServiceId:"+cpServiceId+"] [channelId:"+channelId+"] [p:"+p+"] [region:"+region+"] [Ua:"+Ua+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
		}catch(Exception e){
			PlatformSavingCenter.logger.warn("[移动登陆回调] [返回字符串:"+str+"] [异常]",e);
		}
	}
}
