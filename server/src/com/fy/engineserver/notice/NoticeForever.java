package com.fy.engineserver.notice;


import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.xuanzhi.boss.game.GameConstants;

public class NoticeForever {
	
	private int id;
	
	private String titlename;
	
	private String noticeContent;
	
	private long starttime;
	
	private long endtime;
	
	private String typename;
	
	private String[] openServers;
	
	private String []limitServers;
	
	private Platform[] platformname;

	public boolean isEffective() {
		long now = SystemTime.currentTimeMillis();
		if(starttime > now || endtime < now){
//			System.out.println("不再有效期false。。");
			return false;
		}
		if (!PlatformManager.getInstance().isPlatformOf(platformname)) {
//			System.out.println("平台不符false。。");
			return false;
		}
		GameConstants gc = GameConstants.getInstance();
		if (gc == null) {
//			System.out.println("gc==nullfalse。。");
			return false;
		}
		
		if (openServers != null && openServers.length > 0) {
			for (int i = 0; i < openServers.length; i++) {
				if (gc.getServerName().trim().equals(openServers[i].trim())) {
//					System.out.println("是开放服务器false。。");
					return true;
				}
			}
		} 
		
		if (limitServers != null && limitServers.length > 0) {
			for (int j = 0; j < limitServers.length; j++) {
				if (gc.getServerName().trim().equals(limitServers[j].trim())) {
//					System.out.println("是不开放服务器false。。");
					return false;
				}
			}
		}
		
		/**开放的服务器为空，该平台都符合**/
		if(openServers==null || openServers.length==0){
//			System.out.println("开放的服务器为空，该平台都符合true。。。");
			return true;
		}
		return false;
	}
	
	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public String getTitlename() {
		return titlename;
	}

	public void setTitlename(String titlename) {
		this.titlename = titlename;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	public String[] getOpenServers() {
		return openServers;
	}

	public void setOpenServers(String[] openServers) {
		this.openServers = openServers;
	}

	public String[] getLimitServers() {
		return limitServers;
	}

	public void setLimitServers(String[] limitServers) {
		this.limitServers = limitServers;
	}

	public Platform[] getPlatformname() {
		return platformname;
	}

	public void setPlatformname(Platform[] platformname) {
		this.platformname = platformname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
