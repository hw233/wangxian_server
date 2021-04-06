package com.xuanzhi.tools.text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * 
 */
public class PasswordRecover implements Runnable {
	
	private long okline;

	private HashMap<String,TestPair> lastLoginOkMap = new HashMap<String,TestPair>();
	
	private long failline;
	
	private HashMap<String,TestPair> lastLoginFailMap = new HashMap<String,TestPair>();
	
	private HashSet<String> restoredMap = new HashSet<String>();
	
	private long failButNoOK = 0;
	private long recover = 0;
	private long failButOKLater = 0;
	//private long failButPassNotMatch = 0;
	
	private String okfile = "";
	private String failfile = "";
	private String restoredFile = "";
	
	private static boolean test;
	
	public PasswordRecover(boolean test) {
		PasswordRecover.test = test;
	}
	
	public void start() {
		Thread t = new Thread(this, "PasswordRecover");
		t.start();
	}
	
	public void run() {
		System.out.println("[starting to analysis...]");
		//读入内存
		readOkFile();
		readFailFile();
		readRestoredFile();
		List<TestPair> plist = new ArrayList<TestPair>();
		//从错误的集合开始检查
		TestPair pairs[] = lastLoginFailMap.values().toArray(new TestPair[0]);
		for(TestPair p : pairs) {
			TestPair ok = lastLoginOkMap.get(p.username);
			if(ok == null) {
				failButNoOK++;
				System.out.println("[fail_but_no_ok_found] ["+p.username+"] ["+p.passwd+"] [num:"+failButNoOK+"]");
			} else {
				//如果ok的在前，fail的在后, 恢复为ok，否则不管
				if(p.time > ok.time) {
					if(!restoredMap.contains(p.username)) {
						plist.add(ok);
						recover++;
						System.out.println("[match_recover] ["+recover+"] ["+p.username+"] [fail:"+p.passwd+"] [ok:"+ok.passwd+"] [failTime:"+DateUtil.formatDate(new Date(p.time),"yyyyMMdd_HH:mm:ss")+"] [okTime:"+DateUtil.formatDate(new Date(ok.time),"yyyyMMdd_HH:mm:ss")+"]");
					} else {
						System.out.println("[match_recover_but_already_restored] ["+recover+"] ["+p.username+"] [fail:"+p.passwd+"] [ok:"+ok.passwd+"] [failTime:"+DateUtil.formatDate(new Date(p.time),"yyyyMMdd_HH:mm:ss")+"] [okTime:"+DateUtil.formatDate(new Date(ok.time),"yyyyMMdd_HH:mm:ss")+"]");
					}
				} else if(p.time < ok.time) {
					failButOKLater++;
					System.out.println("[fail_but_ok_later] ["+failButOKLater+"] ["+p.username+"] [fail:"+p.passwd+"] [ok:"+ok.passwd+"] [failTime:"+DateUtil.formatDate(new Date(p.time),"yyyyMMdd_HH:mm:ss")+"] [okTime:"+DateUtil.formatDate(new Date(ok.time),"yyyyMMdd_HH:mm:ss")+"]");
				} 
			}
		}
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@116.213.142.183:1521:ora183", "mieshiboss", "4Fxtkq5J9ydy3sK1");
			for(TestPair p : plist) {
				restorePassword(p, p.passwd, conn);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("[finished] [登陆成功的行:"+okline+"] [登陆成功数量:"+lastLoginOkMap.size()+"] [登陆失败行:"+failline+"] [登陆失败数量:"+lastLoginFailMap.size()+"] [成功重置:"+recover+"] [登陆失败但是未找到成功:"+failButNoOK+"] [登陆失败但是后来成功:"+failButOKLater+"] [已经恢复的:"+restoredMap.size()+"]");
	}
	
	public static void restorePassword(TestPair pair, String password, Connection conn) throws Exception {
		long start = System.currentTimeMillis();
		if(test) {
			System.out.println("[测试恢复执行成功] ["+pair.username+"] [password:"+password+"] [result:0] ["+(System.currentTimeMillis()-start)+"ms]");
		} else {
			PreparedStatement pstmt = null;
			try {
				String sql = "update mieshiboss.passport set passwd=? where username=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, StringUtil.hash(password));
				pstmt.setString(2, pair.username);
				int result = pstmt.executeUpdate();
				//int result = 1;
				System.out.println("[恢复执行成功] ["+pair.username+"] [password:"+password+"] [result:"+result+"] ["+(System.currentTimeMillis()-start)+"ms]");
			} catch(Exception e) {
				System.out.println("[恢复执行失败] ["+pair.username+"] [password:"+password+"]");
				e.printStackTrace();
			} finally {
				try {
					pstmt.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
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
					//System.out.println("[update_OK] ["+time+"] ["+username+"] ["+password+"]");
				} else if(exists == null) {
					TestPair pair = new TestPair(time, username, password);
					lastLoginOkMap.put(username, pair);
					//System.out.println("[new_OK] ["+time+"] ["+username+"] ["+password+"]");
				}
				if(okline % 1000 == 0) {
					System.out.println("[read_ok_file] [okLine:"+okline+"] [oknum:"+lastLoginOkMap.size()+"]");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void readFailFile() {
		String content = FileUtils.readFile(failfile);
		String ss[] = content.split("\n");
		failline = ss.length;
		for(String s : ss) {
			String str[] = s.split(" ");
			String dateStr = str[0] + " " + str[1];
			long time = DateUtil.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss").getTime();
			String username = str[2].substring(10, str[2].length()-1);
			String password = str[3].substring(8, str[3].length()-1);
			TestPair exists = lastLoginFailMap.get(username);
			if(exists != null && time > exists.time) {
				exists.time = time;
				exists.passwd = password;
				System.out.println("[update_FAIL] ["+time+"] ["+username+"] ["+password+"]");
			} else if(exists == null) {
				TestPair pair = new TestPair(time, username, password);
				lastLoginFailMap.put(username, pair);
				System.out.println("[new_FAIL] ["+time+"] ["+username+"] ["+password+"]");
			}
		}
	}
	

	
	private void readRestoredFile() {
		String content = FileUtils.readFile(restoredFile);
		String ss[] = content.split("\n");
		for(String s : ss) {
			restoredMap.add(s.trim());
		}
	}

	public String getOkfile() {
		return okfile;
	}

	public void setOkfile(String okfile) {
		this.okfile = okfile;
	}

	public String getFailfile() {
		return failfile;
	}

	public void setFailfile(String failfile) {
		this.failfile = failfile;
	}
	
	
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
	
	public static void main(String args[]) {
		if(args.length == 0) {
			System.out.println("参数有误");
			return;
		}
		PasswordRecover r = new PasswordRecover(Boolean.valueOf(args[0]));
		r.setOkfile("/home/game/resin_gateway/ok_passwd.txt");
		r.setFailfile("/home/game/resin_gateway/error_passwd.txt");
		r.setRestoredFile("/home/game/resin_gateway/restored.txt");
		r.start();
//		TestPair p = new PasswordRecover.TestPair(0L, "1d3031d", "x003311");
//		Connection conn = null;
//		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//			conn = DriverManager.getConnection(
//				"jdbc:oracle:thin:@116.213.142.183:1521:ora183", "mieshiboss", "4Fxtkq5J9ydy3sK1");
//			restorePassword(p, p.passwd, conn);
//		} catch(Exception e) {
//			e.printStackTrace();
//		} finally {
//			if(conn != null) {
//				try {
//					conn.close();
//				} catch(Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}

	public String getRestoredFile() {
		return restoredFile;
	}

	public void setRestoredFile(String restoredFile) {
		this.restoredFile = restoredFile;
	}
}
