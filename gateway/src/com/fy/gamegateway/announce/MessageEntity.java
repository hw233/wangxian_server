package com.fy.gamegateway.announce;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"createtime"}),
	@SimpleIndex(members={"updatetime"}),
	@SimpleIndex(members={"mesType"})
})
public class MessageEntity {
	@SimpleId
	private long id;
	
	@SimpleVersion
	private int version;
	
	private long createtime;
	private long updatetime;
	
	@SimpleColumn(length=50000)
	private MessageContent messageContent;
	
	private int mesType;
	private int status;
	private String mesTitle;
	
	private String operation;
	
	private long starttime;
	
	private long endtime;
	
	private String defaultProperty1;
	private String defaultProperty2;
	private String defaultProperty3;
	private String defaultProperty4;
	private String defaultProperty5;
	private String defaultProperty6;
	private int serverType;
	
	
	public static int MESSAGE_GONGGAO_TYPE = 1;
	public static int MESSAGE_HEFUTISHI_TYPE = 2;
	
	public static int MESSAGE_INNERTEST_STATUS = -1;
	public static int MESSAGE_NORMAL_STATUS = 1;
	public static int MESSAGE_FREEZE_STATUS = 2;
	
	public static String[]TYPE_NAMES = new String[]{"","公告","合服提示"};
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public MessageContent getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(MessageContent messageContent) {
		this.messageContent = messageContent;
	}
	public int getMesType() {
		return mesType;
	}
	public void setMesType(int mesType) {
		this.mesType = mesType;
	}
	public String getMesTitle() {
		return mesTitle;
	}
	public void setMesTitle(String mesTitle) {
		this.mesTitle = mesTitle;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getDefaultProperty1() {
		return defaultProperty1;
	}
	public void setDefaultProperty1(String defaultProperty1) {
		this.defaultProperty1 = defaultProperty1;
	}
	public String getDefaultProperty2() {
		return defaultProperty2;
	}
	public void setDefaultProperty2(String defaultProperty2) {
		this.defaultProperty2 = defaultProperty2;
	}
	public String getDefaultProperty3() {
		return defaultProperty3;
	}
	public void setDefaultProperty3(String defaultProperty3) {
		this.defaultProperty3 = defaultProperty3;
	}
	public String getDefaultProperty4() {
		return defaultProperty4;
	}
	public void setDefaultProperty4(String defaultProperty4) {
		this.defaultProperty4 = defaultProperty4;
	}
	public String getDefaultProperty5() {
		return defaultProperty5;
	}
	public void setDefaultProperty5(String defaultProperty5) {
		this.defaultProperty5 = defaultProperty5;
	}
	public String getDefaultProperty6() {
		return defaultProperty6;
	}
	public void setDefaultProperty6(String defaultProperty6) {
		this.defaultProperty6 = defaultProperty6;
	}
	
	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	
	public int getServerType() {
		return serverType;
	}
	public void setServerType(int serverType) {
		this.serverType = serverType;
	}
	public String getLogString()
	{
		String logContent = "["+id+"] ["+version+"] ["+createtime+"] ["+updatetime+"] ["+messageContent+"] ["+mesTitle+"] ["+mesType+"] ["+serverType+"] ["+status+"] ["+defaultProperty1+"] ["+defaultProperty2+"] ["+defaultProperty3+"] ["+defaultProperty4+"] ["+defaultProperty5+"] ["+defaultProperty6+"]";
		return logContent;
	}
}
