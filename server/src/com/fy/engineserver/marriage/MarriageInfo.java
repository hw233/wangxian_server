package com.fy.engineserver.marriage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class MarriageInfo {
	
	public static final byte STATE_SHIBAI = -1;			//失败
	public static final byte STATE_START = 0;			//开始
	public static final byte STATE_JIEHUN = 1;			//结婚		婚礼结束后就是此状态
	public static final byte STATE_LIHUN = 2;			//离婚
	
	public static final byte STATE_CHOOSE_LEVEL = 100;	//选择婚礼规模完成
	public static final byte STATE_CHOOSE_GUEST = 101;	//选择嘉宾完成
	public static final byte STATE_CHOOSE_TIME = 102;	//选择时间完成
	
	public static final byte STATE_HUNLI_START = 103;	//婚礼正式开始
	public static final byte STATE_UNONLINE = 104;		//婚礼开始后人未在线
	public static final byte STATE_DROP = 105;			//掉物品
	
	
	@SimpleId
	private long id;
	
	@SimpleVersion
	private int versionMInfo;
	
	private int delayNum = -1; // 推迟次数，只能有一次
	@SimpleColumn(name="level2")
	private int level;				//婚礼级别
	
	private long holdA;				//角色ID
	private long holdB;				//角色ID
	@SimpleColumn(length=1000)
	private ArrayList<Long> guestA;		//A邀请的嘉宾
	private transient boolean guestOverA = false;	//选择宾客结束
	@SimpleColumn(length=1000)
	private ArrayList<Long> guestB;		//B邀请的宾客
	private transient boolean guestOverB = false;	//选择宾客结束
	
	private long beginTime;  			// 多长时间后开始   开始前半个小时  可以进入副本
	
	private long startTime;				//婚礼具体什么时候开始
	
	private long successTime;  			//婚礼完成时间
	
	private long dropTime;				//发物品的时间
	
	private int dropNum;				//发物品结束时间
	
	private int sendMessage2GuestTimeIndex = -1;			//发送到时邀请发送到第几个时刻
	
	private transient boolean isjiaohuan = false;			//是否一方确定交换戒指
	
	private long cityId;				//副本ID
	
	private byte state;					//结婚状态
	
	private int timeLevel = -1;			//结婚周年  第几周年
	
	private transient long cacheTime;	//释放时间
	
	public String getLogString(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String starttimeString = "0";
		if (startTime > 0) {
			starttimeString = format.format(new Date(startTime));
		}
		String sucessTimeString = "0";
		if (successTime > 0) {
			sucessTimeString = format.format(new Date(successTime));
		}
		String dropTimeString = "0";
		if (dropTime > 0) {
			dropTimeString = format.format(new Date(dropTime));
		}
		
		StringBuffer sb = new StringBuffer("");
		sb.append("[id=").append(id)
		.append("] [A=").append(holdA)
		.append("] [B=").append(holdB)
		.append("] [level=").append(level)
		.append("] [state=").append(state)
		.append("] [cityID=").append(cityId)
		.append("] [周年=").append(timeLevel)
		.append("] [推迟=").append(delayNum)
		.append("] [迟Time=").append(beginTime)
		.append("] [邀请次数=").append(sendMessage2GuestTimeIndex)
		.append("] [婚礼开始=").append(starttimeString)
		.append("] [婚礼完成=").append(sucessTimeString)
		.append("] [发掉落=").append(dropTimeString).append("/").append(dropNum)
		.append("] [guestAOK=").append(guestOverA)
		.append("] [guestBOK=").append(guestOverB);
		if (MarriageManager.logger.isInfoEnabled()) {
			StringBuffer sbA = new StringBuffer("");
			for (int i = 0; i < guestA.size(); i++) {
				sbA.append(guestA.get(i)).append(",");
			}
			StringBuffer sbB = new StringBuffer("");
			for (int i = 0; i < guestB.size(); i++) {
				sbB.append(guestB.get(i)).append(",");
			}
			sb.append("] [guestA=").append(sbA.toString())
			.append("] [guestB=").append(sbB.toString())
			;
		}
		return sb.toString();
	}
	
	public MarriageInfo(){
		setGuestA(new ArrayList<Long>());
		setGuestB(new ArrayList<Long>());
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
		MarriageManager.getInstance().emInfo.notifyFieldChange(this, "level");
	}

	public long getHoldA() {
		return holdA;
	}
	public void setHoldA(long holdA) {
		this.holdA = holdA;
	}
	public long getHoldB() {
		return holdB;
	}
	public void setHoldB(long holdB) {
		this.holdB = holdB;
	}
	public int getDelayNum() {
		return delayNum;
	}
	public void setDelayNum(int delayNum) {
		this.delayNum = delayNum;
		MarriageManager.getInstance().emInfo.notifyFieldChange(this, "delayNum");
	}
	public long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
		MarriageManager.getInstance().emInfo.notifyFieldChange(this, "beginTime");
	}
	public long getSuccessTime() {
		return successTime;
	}
	public void setSuccessTime(long successTime) {
		this.successTime = successTime;
		MarriageManager.getInstance().emInfo.notifyFieldChange(this, "successTime");
	}
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
		MarriageManager.getInstance().emInfo.notifyFieldChange(this, "cityId");
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public int getTimeLevel() {
		return timeLevel;
	}

	public void setTimeLevel(int timeLevel) {
		if (this.timeLevel == timeLevel) {
			return;
		}
		this.timeLevel = timeLevel;
		MarriageManager.getInstance().emInfo.notifyFieldChange(this, "timeLevel");
	}

	public void setState(byte state) {
		if (this.state == state) {
			return;
		}
		this.state = state;
		MarriageManager.getInstance().emInfo.notifyFieldChange(this, "state");
	}

	public byte getState() {
		return state;
	}

	public void setGuestA(ArrayList<Long> guest) {
		this.guestA = guest;
	}

	public ArrayList<Long> getGuestA() {
		return guestA;
	}

	public void setGuestB(ArrayList<Long> guestB) {
		this.guestB = guestB;
	}

	public ArrayList<Long> getGuestB() {
		return guestB;
	}
	
	public void getGuestCareer(boolean isA,byte[] career){
		if(isA){
			for(int i = 0; i< getGuestA().size(); i++){
				PlayerSimpleInfo playInfo = PlayerSimpleInfoManager.getInstance().getInfoById(getGuestA().get(i));
				if (playInfo == null) {
					career[i] = 0;
				}else {
					career[i] = playInfo.getCareer();
				}
			}
			for(int i = 0; i< getGuestB().size(); i++){
				int n = getGuestA().size()+i;
				PlayerSimpleInfo playInfo = PlayerSimpleInfoManager.getInstance().getInfoById(getGuestB().get(i));
				if (playInfo == null) {
					career[n] = 0;
				}else {
					career[n] = playInfo.getCareer();
				}
				
			}
		}else{
			for(int i = 0; i< getGuestB().size(); i++){
				PlayerSimpleInfo playInfo = PlayerSimpleInfoManager.getInstance().getInfoById(getGuestB().get(i));
				if (playInfo == null) {
					career[i] = 0;
				} else {
					career[i] = playInfo.getCareer();
				}
			}
			for(int i = 0; i< getGuestA().size(); i++){
				int n = getGuestB().size()+i;
				PlayerSimpleInfo playInfo = PlayerSimpleInfoManager.getInstance().getInfoById(getGuestA().get(i));
				if (playInfo == null) {
					career[n] = 0;
				} else {
					career[n] = playInfo.getCareer();
				}
				
			}
		}
	}

	public void getGuest(boolean isA, byte[] bs, long[] ids, String[] names){
		if(isA){
			for(int i = 0; i< getGuestA().size(); i++){
				if(bs!=null){
					bs[i] = 0;
				}
				if(ids!=null){
					ids[i] = getGuestA().get(i);
				}
				
				PlayerSimpleInfo playInfo = PlayerSimpleInfoManager.getInstance().getInfoById(getGuestA().get(i));
				if (playInfo == null) {
					names[i] = "未知";
				} else {
					names[i] = playInfo.getName();
				}
			}
			for(int i = 0; i< getGuestB().size(); i++){
				int n = getGuestA().size()+i;
				if(bs!=null){
					bs[n] = 1;
				}
				if(ids!=null){
					ids[n] = getGuestB().get(i);
				}
				
				PlayerSimpleInfo playInfo = PlayerSimpleInfoManager.getInstance().getInfoById(getGuestB().get(i));
				if (playInfo == null) {
					names[n] = "未知";
				} else {
					names[n] = playInfo.getName();
				}
			}
		}else{
			for(int i = 0; i< getGuestB().size(); i++){
				if(bs!=null){
					bs[i] = 0;
				}
				if(ids!=null){
					ids[i] = getGuestB().get(i);
				}

				PlayerSimpleInfo playInfo = PlayerSimpleInfoManager.getInstance().getInfoById(getGuestB().get(i));
				if (playInfo == null) {
					names[i] = "未知";
				} else {
					names[i] = playInfo.getName();
				}
			}
			for(int i = 0; i< getGuestA().size(); i++){
				int n = getGuestB().size()+i;
				if(bs!=null){
					bs[n] = 1;
				}
				if(ids!=null){
					ids[n] = getGuestA().get(i);
				}
				
				PlayerSimpleInfo playInfo = PlayerSimpleInfoManager.getInstance().getInfoById(getGuestA().get(i));
				if (playInfo == null) {
					names[n] = "未知";
				} else {
					names[n] = playInfo.getName();
				}
			}
		}
	}

	public void setGuestOverA(boolean guestOverA) {
		this.guestOverA = guestOverA;
	}

	public boolean isGuestOverA() {
		return guestOverA;
	}

	public void setGuestOverB(boolean guestOverB) {
		this.guestOverB = guestOverB;
	}

	public boolean isGuestOverB() {
		return guestOverB;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
		MarriageManager.getInstance().emInfo.notifyFieldChange(this, "startTime");
	}

	public long getStartTime() {
		return startTime;
	}

	public void setSendMessage2GuestTimeIndex(int sendMessage2GuestTimeIndex) {
		this.sendMessage2GuestTimeIndex = sendMessage2GuestTimeIndex;
		MarriageManager.getInstance().emInfo.notifyFieldChange(this, "sendMessage2GuestTimeIndex");
	}

	public int getSendMessage2GuestTimeIndex() {
		return sendMessage2GuestTimeIndex;
	}

	public void setIsjiaohuan(boolean isjiaohuan) {
		this.isjiaohuan = isjiaohuan;
	}

	public boolean isIsjiaohuan() {
		return isjiaohuan;
	}

	public void setDropTime(long dropTime) {
		this.dropTime = dropTime;
		MarriageManager.getInstance().emInfo.notifyFieldChange(this, "dropTime");
	}

	public long getDropTime() {
		return dropTime;
	}

	public void setDropNum(int dropNum) {
		this.dropNum = dropNum;
		MarriageManager.getInstance().emInfo.notifyFieldChange(this, "dropNum");
	}

	public int getDropNum() {
		return dropNum;
	}

	public void setVersionMInfo(int versionMInfo) {
		this.versionMInfo = versionMInfo;
	}

	public int getVersionMInfo() {
		return versionMInfo;
	}

	public void setCacheTime(long cacheTime) {
		this.cacheTime = cacheTime;
	}

	public long getCacheTime() {
		return cacheTime;
	}

}
