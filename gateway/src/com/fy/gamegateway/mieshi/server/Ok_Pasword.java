package com.fy.gamegateway.mieshi.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.FileUtils;


public class Ok_Pasword {
	
	public static class TestPair {
		public long time;
		public String username;
		public String passwd;
		
		public TestPair(long time, String username, String password) {
			this.time = time;
			this.username = username;
			this.passwd = password;
		}
	}

	public String okfile = "/home/game/resin_gateway/webapps/game_gateway/WEB-INF/ok_passwd.txt";
	public String restoredFile = "/home/game/resin_gateway/webapps/game_gateway/WEB-INF/restored.txt";
	public long okline = 0;
	
	public HashMap<String,TestPair> lastLoginOkMap = new HashMap<String,TestPair>();
	
	public DefaultDiskCache restoreCache ;
	
	/**
	 * 判断最后一次成功登录的密码，和这次登录失败的密码是否一致？
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean isNeedRestore(String username,String password){
		if(restoreCache.get(username) != null) return false;
		if(lastLoginOkMap.containsKey(username)){
			TestPair t = (TestPair)lastLoginOkMap.get(username);
			if(t.passwd.equals(password)){
				return true;
			}
		}
		return false;
	}
	
	public void hadRestored(String username,String password){
		restoreCache.put(username, password);
	}
	
	public void init(){
		long startTime = System.currentTimeMillis();
		System.out.println("[初始化OK_Password] [开始] [.......]");
		
		restoreCache = new DefaultDiskCache(new File("/home/game/resin_gateway/webapps/game_gateway/WEB-INF/diskCacheFileRoot/mieshi_password_restore.ddc"),"mieshi-passwordrestore-cache",365L*24*3600*1000L);
		
		restoreCache.setMaxDiskSize(10L*1024*1024*1024);
		restoreCache.setMaxMemorySize(64*1024*1024);
		restoreCache.setMaxElementNum(1024*1024*1024);
		
		
		readRestoredFile();
		
		readOkFile();
		
	
		System.out.println("[初始化OK_Password] [完成] [ok_passwd:"+lastLoginOkMap.size()+"] [restore:"+restoreCache.getNumElements()+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
	}
	
	private void readRestoredFile() {
		String content = FileUtils.readFile(restoredFile);
		String ss[] = content.split("\n");
		for(String s : ss) {
			if(restoreCache.get(s.trim()) == null){
				restoreCache.put(s.trim(), "");
			}
		}
	}
	
	private void readOkFile() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(okfile));
			String line = null;
			while((line = reader.readLine()) != null) {
				okline++;
				String str[] = line.split(" ");
				if(str.length < 4) {
					continue;
				}
				String dateStr = str[0] + " " + str[1];
				long time = DateUtil.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss").getTime();
				String username = str[2].substring(10, str[2].length()-1);
				String password = str[3].substring(8, str[3].length()-1);
				TestPair exists = lastLoginOkMap.get(username);
				if(exists != null && time > exists.time) {
					exists.time = time;
					exists.passwd = password;
				} else if(exists == null) {
					TestPair pair = new TestPair(time, username, password);
					lastLoginOkMap.put(username, pair);
				}
				if(okline % 1000000 == 0) {
					System.out.println("[read_ok_file] [okLine:"+okline+"] [oknum:"+lastLoginOkMap.size()+"]");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
