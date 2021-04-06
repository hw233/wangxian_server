package com.fy.gamegateway.mieshi.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
/**
 * 
 * 
 *
 */
public class MieshiServerInfoManager{
	
	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);
	
	private static MieshiServerInfoManager self;
	public static MieshiServerInfoManager getInstance(){
		return self;
	}
	String cacheFile ;
	
	/**
	 * 服务器大区分类
	 */
	ArrayList<String> serverCategories = new ArrayList<String>();
	
	ArrayList<MieshiServerInfo> serverList = new ArrayList<MieshiServerInfo>();
	
	ArrayList<TestUser> testUsernameList = new ArrayList<TestUser>();
	
	//推荐服务器，即新的玩家进入服务器列表，推荐这些
	ArrayList<MieshiServerInfo> tuijianServerInfo = new ArrayList<MieshiServerInfo>();
	
	ArrayList<MieshiServerInfo> newServerInfo = new ArrayList<MieshiServerInfo>();
	
	public static class TestUser implements java.io.Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7166934834430476798L;
		public String username;
		public String reason;
		public String gm;
		public boolean enable;
		public Date createTime;
		public Date lastModifiedTime;
		public String addUser;
		public String bumen;
	}
	
//	public static class DenyUser implements java.io.Serializable{
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -3237631245900326010L;
//		public String deviceId;
//		public String clientId;
//		public String username;
//		public String reason;
//		public String innerReason;
//		public String gm;
//		public boolean denyClientId = false;
//		public boolean denyDeviceId = false;
//		public boolean enableDeny = false;
//		public boolean foroverDeny = false;
//		public long denyStartTime;
//		public long denyEndTime;
//		
//	}
	
	public DefaultDiskCache cache ;
	public static final String KEY_SERVER_LIST = "serverList";
	public static final String KEY_SERVER_CATEGORIES = "serverCategories";
	public static final String KEY_TEST_USERNAME = "testUserList";
	
	public static final String KEY_DENY_USERNAME = "testDenyUserList";
	
	public static final String KEY_TUIJIAN_SERVER = "tuijianServerNameList";
	
	public static final String KEY_NEW_SERVER = "newServerNameList";
	
	
	public void init(){
		cache = new DefaultDiskCache(new File(cacheFile),"mieshi-server-info-cache",100L*365*24*3600*1000L);
		serverList = (ArrayList<MieshiServerInfo>)cache.get(KEY_SERVER_LIST);
		serverCategories = (ArrayList<String>)cache.get(KEY_SERVER_CATEGORIES);
		testUsernameList = (ArrayList<TestUser>)cache.get(KEY_TEST_USERNAME);
		
		ArrayList<String> al = (ArrayList<String>)cache.get(KEY_TUIJIAN_SERVER);
		if(al == null) al = new ArrayList<String>();
		
		if(serverCategories == null){
			serverCategories = new ArrayList<String>();
		} 
		
		if(serverList == null){
			serverList = new ArrayList<MieshiServerInfo>();
		}
		
		if(testUsernameList == null){
			testUsernameList = new ArrayList<TestUser>();
		}
		
		if(tuijianServerInfo == null){
			serverList = new ArrayList<MieshiServerInfo>();
		}
		
		for(String s : al){
			MieshiServerInfo si = this.getServerInfoByName(s);
			if(si != null){
				tuijianServerInfo.add(si);
			}
		}
		
		self = this;
	}
	
	public String getCacheFile() {
		return cacheFile;
	}

	public void setCacheFile(String cacheFile) {
		this.cacheFile = cacheFile;
	}

	/**
	 * 是否为测试用户
	 * @param userName
	 * @return
	 */
	public boolean isTestUser(String userName){
//		if(userName.toLowerCase().contains("vivo")){
//			return true;
//		}
		for(TestUser tu : testUsernameList){
			if(tu.username != null && tu.username.trim().equals(userName)){
				
				if(tu.enable){
					return true;
				}
			}
		}
		return false;
	}
	
	public TestUser getTestUser(String userName){
		for(TestUser tu : testUsernameList){
			if(tu.username != null && tu.username.trim().equals(userName)){
				return tu;
			}
		}
		return null;
	}
	
	public ArrayList<TestUser> getTestUsers(){
		return testUsernameList;
	}
	
	public DenyUserEntity getDenyUser(String username){
		DenyUserEntity denyUserEntity = getDenyUserEntity(username);
		if(denyUserEntity !=  null) {
			return denyUserEntity;
		}
	
		return null;
	}
	
	public DenyUserEntity getDenyUserEntity(String username) {
		DenyUserEntityManager denyUserEntityManager = DenyUserEntityManager.getInstance();
		
		if(username != null)
		{
			try {
				List<DenyUserEntity> lst =  denyUserEntityManager.queryForWhere("username = ?", new Object[]{username});
				if(lst != null && lst.size() > 0)
				{
					return lst.get(0);
				}
				
			} catch (Exception e) {
				return null;
			}
			
			return null;
		
		}
		return null;
	}
	
	public void removeDenyUser(String username){
		
		DenyUserEntityManager denyUserEntityManager = DenyUserEntityManager.getInstance();
		
		DenyUserEntity du = getDenyUser(username);
		if(du != null){
			try{
				denyUserEntityManager.em.remove(du);		
			}catch(Exception e) {
				logger.error(e);
			}
		}
	}
	
	public ArrayList<DenyUserEntity> getDenyUserList() {
		DenyUserEntityManager dm = DenyUserEntityManager.getInstance();
		ArrayList<DenyUserEntity> ds = new ArrayList<DenyUserEntity>();
		try{
			long count = dm.em.count();
			int index = 1;
			while(index <= count){
				ds.addAll(dm.em.query(DenyUserEntity.class, "", "", index, index + 500));
				index += 500;
			}
		}catch(Exception e) {
			logger.error(e);
		}
		return ds;
	}
	
	public void clearTestUser(){
		ArrayList<TestUser> lst = (ArrayList<TestUser>)cache.get(KEY_TEST_USERNAME);
		if(lst != null)
		{
			lst.clear();
			
		}
		else
		{
			lst = new ArrayList<MieshiServerInfoManager.TestUser>();
		}
		cache.put(KEY_TEST_USERNAME, lst);
	}
	
	/**
	 * 增加一个限制用户
	 * @param username
	 * @param reason
	 * @param gm
	 * @param foroverDeny
	 * @param denyDays
	 * @param hours
	 */
	public void addDenyUser(String deviceId,String clientId,boolean denyDeviceId,boolean denyClientId,String username, String reason,
			String innerReason,String gm,boolean foroverDeny,int denyDays,int hours){
		DenyUserEntity du = getDenyUser(username);
		if(du != null){
			du.clientId = clientId;
			du.deviceId = deviceId;
			du.denyClientId = denyClientId;
			du.denyDeviceId = denyDeviceId;
			
			du.enableDeny = true;
			du.foroverDeny = foroverDeny;
			du.denyStartTime = System.currentTimeMillis();
			du.denyEndTime = du.denyStartTime + 1L * (denyDays * 24  + hours)* 3600000 ;
			du.gm = gm;
			du.username = username;
			du.reason = reason;
			du.innerReason = innerReason;
		}else{
			du = new DenyUserEntity();
			try{				
				du.id = DenyUserEntityManager.getInstance().em.nextId();
			}catch(Exception e){
				logger.error("[封号ID生成出错]" + e);
			}
			du.deviceId = deviceId;
			du.denyDeviceId = denyDeviceId;
			du.clientId = clientId;
			du.denyClientId = denyClientId;
			
			du.enableDeny = true;
			du.foroverDeny = foroverDeny;
			du.denyStartTime = System.currentTimeMillis();
			du.denyEndTime = du.denyStartTime + 1L * (denyDays * 24  + hours)* 3600000 ;
			du.gm = gm;
			du.username = username;
			du.reason = reason;
			du.innerReason = innerReason;
		}
		try {
			DenyUserEntityManager.getInstance().em.flush(du);
		} catch (Exception e) {
			logger.error("[封号保存出错]" + e);
		}
		
	}
	/**
	 * 增加一个限制用户
	 * @param username
	 * @param reason
	 * @param gm
	 * @param foroverDeny
	 * @param denyDays
	 * @param hours
	 */
	public void addDenyUser(String deviceId,String clientId,boolean denyDeviceId,boolean denyClientId,String username, String reason,
			String gm,boolean foroverDeny,int denyDays,int hours){
		DenyUserEntity du = getDenyUser(username);
		if(du != null){
			du.clientId = clientId;
			du.deviceId = deviceId;
			du.denyClientId = denyClientId;
			du.denyDeviceId = denyDeviceId;
			
			du.enableDeny = true;
			du.foroverDeny = foroverDeny;
			du.denyStartTime = System.currentTimeMillis();
			du.denyEndTime = du.denyStartTime + 1L * (denyDays * 24  + hours)* 3600000 ;
			du.gm = gm;
			du.username = username;
			du.reason = reason;
		}else{
			du = new DenyUserEntity();
			try{				
				du.id = DenyUserEntityManager.getInstance().em.nextId();
			}catch(Exception e){
				logger.error("[封号ID生成出错]" + e);
			}
			du.deviceId = deviceId;
			du.denyDeviceId = denyDeviceId;
			du.clientId = clientId;
			du.denyClientId = denyClientId;
			
			du.enableDeny = true;
			du.foroverDeny = foroverDeny;
			du.denyStartTime = System.currentTimeMillis();
			du.denyEndTime = du.denyStartTime + 1L * (denyDays * 24  + hours)* 3600000 ;
			du.gm = gm;
			du.username = username;
			du.reason = reason;
		}
		try {
			DenyUserEntityManager.getInstance().em.flush(du);
		} catch (Exception e) {
			logger.error("[封号保存出错]" + e);
		}
		logger.warn("[封禁一个用户] [username:"+du.username+"] [reason:"+du.reason+"] [inner:"+du.innerReason+"] [forever"+du.foroverDeny+"] [operator:"+du.gm+"]");
	}
	
	public void disableDenyUser(String username){
		DenyUserEntity du = getDenyUser(username);
		if(du != null){
			du.enableDeny = false;
			try {
				DenyUserEntityManager.getInstance().em.flush(du);
			} catch (Exception e) {
				logger.error("[封号保存出错]" + e);
			}
		}
	}
//	public void disableDenyUserByClientId(String clientId){
//		DenyUser du = getDenyUserByClientId(clientId);
//		if(du != null){
//			du.enableDeny = false;
//			cache.put(KEY_DENY_USERNAME,denyUserList);
//			
//			DenyUserEntityManager denyUserEntityManager = DenyUserEntityManager.getInstance();
//			DenyUserEntity denyUserEntity = getDenyUserEntity(clientId);
//			if(denyUserEntity != null)
//			{
//				try {
//					denyUserEntity.enableDeny = du.enableDeny;
//					denyUserEntityManager.saveOrUpdate(denyUserEntity);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//		}
//	}

	public void removeTestUser(String userName){
		TestUser tu = getTestUser(userName);
		if(tu != null){
			this.testUsernameList.remove(tu);
			cache.put(KEY_TEST_USERNAME,testUsernameList);
		}
	}
	
	public void addTestUser(String userName,String reason,boolean enable){
		TestUser tu = getTestUser(userName);
		if(tu != null){
			tu.reason = reason;
			tu.enable = enable;
			tu.lastModifiedTime = new Date();
		}else{
			tu = new TestUser();
			tu.username = userName;
			tu.reason = reason;
			tu.enable = enable;
			tu.lastModifiedTime = new Date();
			tu.createTime = new Date();
			this.testUsernameList.add(tu);
		}
		cache.put(KEY_TEST_USERNAME,testUsernameList);
	}
	
	public void addTestUser(String userName,String reason,boolean enable,String adduser,String bumen){
		TestUser tu = getTestUser(userName);
		if(tu != null){
			tu.reason = reason;
			tu.enable = enable;
			tu.lastModifiedTime = new Date();
		}else{
			tu = new TestUser();
			tu.username = userName;
			tu.reason = reason;
			tu.enable = enable;
			tu.lastModifiedTime = new Date();
			tu.createTime = new Date();
			tu.addUser = adduser;
			tu.bumen = bumen;
			this.testUsernameList.add(tu);
		}
		cache.put(KEY_TEST_USERNAME,testUsernameList);
	}
	
	public void addTestUser(String userName,String reason,boolean enable,String adduser){
		TestUser tu = getTestUser(userName);
		if(tu != null){
			tu.reason = reason;
			tu.enable = enable;
			tu.lastModifiedTime = new Date();
		}else{
			tu = new TestUser();
			tu.username = userName;
			tu.reason = reason;
			tu.enable = enable;
			tu.lastModifiedTime = new Date();
			tu.createTime = new Date();
			tu.addUser = adduser;
			this.testUsernameList.add(tu);
		}
		cache.put(KEY_TEST_USERNAME,testUsernameList);
	}
	
	public void addCategory(String c){
		if(c == null || c.trim().length() == 0) return;
		c = c.trim();
		if(serverCategories.contains(c) == false){
			serverCategories.add(c);
		}
		cache.put(KEY_SERVER_CATEGORIES,serverCategories);
	}
	
	public void setServerCategories(ArrayList<String> al){
		serverCategories = al;
		cache.put(KEY_SERVER_CATEGORIES,serverCategories);
	}

	public void updateServerInfo(MieshiServerInfo info){
		Iterator<MieshiServerInfo> it = serverList.iterator();
		while(it.hasNext()){
			MieshiServerInfo i = it.next();
			if(i.getRealname().equals(info.getRealname())){
				it.remove();
			}
		}
		serverList.add(info);
		cache.put(KEY_SERVER_LIST,serverList);
	}
	
	
	public void removeServerInfo(MieshiServerInfo info){
		serverList.remove(info);
		cache.put(KEY_SERVER_LIST,serverList);
	}
	
	public MieshiServerInfo[] getServerInfoList(){
		return serverList.toArray(new MieshiServerInfo[0]);
	}
	
	public MieshiServerInfo[] getTuijianServerInfoList(){
		return this.tuijianServerInfo.toArray(new MieshiServerInfo[0]);
	}
	
	public MieshiServerInfo[] getNewServerInfoList(){
		return this.newServerInfo.toArray(new MieshiServerInfo[0]);
	}
	
	public void addTuijianServerInfo(String realName){
		MieshiServerInfo si = getServerInfoByName(realName);
		if(si == null) return;
		if(tuijianServerInfo.contains(si) == false){
			tuijianServerInfo.add(si);
		}
		ArrayList<String> al = new ArrayList<String>();
		for(MieshiServerInfo s : tuijianServerInfo){
			al.add(s.getRealname());
		}
		cache.put(KEY_TUIJIAN_SERVER,al);
	}
	
	public void addNewServerInfo(String realName){
		MieshiServerInfo si = getServerInfoByName(realName);
		if(si == null) return;
		if(newServerInfo.contains(si) == false){
			newServerInfo.add(si);
		}
		ArrayList<String> al = new ArrayList<String>();
		for(MieshiServerInfo s : newServerInfo){
			al.add(s.getRealname());
		}
		cache.put(KEY_NEW_SERVER,al);
	}
	
	public void removeTuijianServerInfo(String realName){
		MieshiServerInfo si = getServerInfoByName(realName);
		if(si == null) return;
		tuijianServerInfo.remove(si);
		ArrayList<String> al = new ArrayList<String>();
		for(MieshiServerInfo s : tuijianServerInfo){
			al.add(s.getRealname());
		}
		cache.put(KEY_TUIJIAN_SERVER,al);
	}
	
	public void removeNewServerInfo(String realName){
		MieshiServerInfo si = getServerInfoByName(realName);
		if(si == null) return;
		newServerInfo.remove(si);
		ArrayList<String> al = new ArrayList<String>();
		for(MieshiServerInfo s : newServerInfo){
			al.add(s.getRealname());
		}
		cache.put(KEY_NEW_SERVER,al);
	}
	
	
	public MieshiServerInfo getServerInfoByName(String realName){
		for(MieshiServerInfo m : serverList){
			if(m.getRealname().equals(realName)){
				return m;
			}
		}
		return null;
	}
	
	public MieshiServerInfo[] getServerInfoListByCategory(String c){
		ArrayList<MieshiServerInfo> al = new ArrayList<MieshiServerInfo>();
		for(MieshiServerInfo i : serverList){
			if(i.getCategory() != null && i.getCategory().equals(c)){
				al.add(i);
			}
		}
		return al.toArray(new MieshiServerInfo[0]);
	}
	
	public MieshiServerInfo[] getServerInfoSortedList(){
		MieshiServerInfo ss[] =  serverList.toArray(new MieshiServerInfo[0]);
		Arrays.sort(ss,new Comparator<MieshiServerInfo>(){

			@Override
			public int compare(MieshiServerInfo o1, MieshiServerInfo o2) {
				if(o1.getPriority() > o2.getPriority()){
					return -1;
				}else if(o1.getPriority() < o2.getPriority()){
					return 1;
				}
				return 0;
			}});
		return ss;
	}
	
	public MieshiServerInfo[] getServerInfoSortedListByCategory(String category){
		MieshiServerInfo ss[] =  getServerInfoListByCategory(category);
		Arrays.sort(ss,new Comparator<MieshiServerInfo>(){
			@Override
			public int compare(MieshiServerInfo o1, MieshiServerInfo o2) {
				if(o1.getPriority() > o2.getPriority()){
					return -1;
				}else if(o1.getPriority() < o2.getPriority()){
					return 1;
				}
				return 0;
			}});
		return ss;
	}
	
	public String[] getServerCategories(){
		 return serverCategories.toArray(new String[0]);
	}
	
}
