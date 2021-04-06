package com.fy.gamegateway.mieshi.server;

import java.lang.reflect.Field;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"username"}),
	@SimpleIndex(members={"deviceId"}),
	@SimpleIndex(members={"clientId"})
})
public class DenyUserEntity {
	@SimpleId
	protected long id;
	
	@SimpleVersion
	protected int version;
	
	protected String deviceId;
	protected String clientId;
	protected String username;
	protected String reason;
	@SimpleColumn(length=1024)
	protected String innerReason;
	protected String gm;
	protected boolean denyClientId = false;
	protected boolean denyDeviceId = false;
	protected boolean enableDeny = false;
	protected boolean foroverDeny = false;
	protected long denyStartTime;
	protected long denyEndTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getInnerReason() {
		return innerReason;
	}
	public void setInnerReason(String innerReason) {
		this.innerReason = innerReason;
	}
	public String getGm() {
		return gm;
	}
	public void setGm(String gm) {
		this.gm = gm;
	}
	public boolean isDenyClientId() {
		return denyClientId;
	}
	public void setDenyClientId(boolean denyClientId) {
		this.denyClientId = denyClientId;
	}
	public boolean isDenyDeviceId() {
		return denyDeviceId;
	}
	public void setDenyDeviceId(boolean denyDeviceId) {
		this.denyDeviceId = denyDeviceId;
	}
	public boolean isEnableDeny() {
		return enableDeny;
	}
	public void setEnableDeny(boolean enableDeny) {
		this.enableDeny = enableDeny;
	}
	public boolean isForoverDeny() {
		return foroverDeny;
	}
	public void setForoverDeny(boolean foroverDeny) {
		this.foroverDeny = foroverDeny;
	}
	public long getDenyStartTime() {
		return denyStartTime;
	}
	public void setDenyStartTime(long denyStartTime) {
		this.denyStartTime = denyStartTime;
	}
	public long getDenyEndTime() {
		return denyEndTime;
	}
	public void setDenyEndTime(long denyEndTime) {
		this.denyEndTime = denyEndTime;
	}
	
	public String getLogString()
	{
		Field[] fields = this.getClass().getDeclaredFields();
		
		for(Field f : fields)
		{
			f.setAccessible(true);
		}
		
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i < fields.length;i++)
		{
			Field f = fields[i];
			if(i == 0)
			{
				try {
					buffer.append("[").append(f.getName()+":").append(f.get(this)).append("]");
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					buffer.append("");
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					buffer.append("");
				}
			}
			else
			{
				try {
					buffer.append(" [").append(f.getName()+":").append(f.get(this)).append("]");
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					buffer.append("");
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					buffer.append("");
				}
			}
		}
		
		return buffer.toString();
	}
	
	
	
}
