package com.fy.boss.operation.model;

import java.util.Date;
import java.util.List;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;
import com.xuanzhi.tools.text.DateUtil;

@SimpleEntity(name="GameActivity")
@SimpleIndices({
	@SimpleIndex(members={"activityName"}),
	@SimpleIndex(members={"startTime"}),
	@SimpleIndex(members={"endTime"}),
	@SimpleIndex(members={"platForm"}),
	@SimpleIndex(members={"status"})
})

public class GameActivity {

	@SimpleId
	private long id;

	@SimpleVersion
	private int version;

	private String activityName;
	private long startTime;
	private long endTime;
	@SimpleColumn(length=4000)
	private String activityDesc;
	private String operationPerson;
	private long operatedTime;
	private String platForm;
	private List<String> operationHistoryRecords;
	private List<String> open4Servers;
	private List<String> notOpen4Servers;
	private List<String> activityTables;
	private int status;
	private String color;

	public static int ACTIVITY_WAIT_START = 0;
	public static int ACTIVITY_STARTED = 1;
	public static int ACTIVITY_END = 2;
	public static int ACTIVITY_PAUSE = 3;
	public static int ACTIVITY_EXCEPTION = -1;
	




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
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getActivityDesc() {
		return activityDesc;
	}
	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}
	public String getOperationPerson() {
		return operationPerson;
	}
	public void setOperationPerson(String operationPerson) {
		this.operationPerson = operationPerson;
	}
	public long getOperatedTime() {
		return operatedTime;
	}
	public void setOperatedTime(long operatedTime) {
		this.operatedTime = operatedTime;
	}
	public String getPlatForm() {
		return platForm;
	}
	public void setPlatForm(String platForm) {
		this.platForm = platForm;
	}
	public List<String> getOperationHistoryRecords() {
		return operationHistoryRecords;
	}
	public void setOperationHistoryRecords(
			List<String> operationHistoryRecords) {
		this.operationHistoryRecords = operationHistoryRecords;
	}
	public List<String> getOpen4Servers() {
		return open4Servers;
	}
	public void setOpen4Servers(List<String> open4Servers) {
		this.open4Servers = open4Servers;
	}
	public List<String> getNotOpen4Servers() {
		return notOpen4Servers;
	}
	public void setNotOpen4Servers(List<String> notOpen4Servers) {
		this.notOpen4Servers = notOpen4Servers;
	}
	public List<String> getActivityTables() {
		return activityTables;
	}
	public void setActivityTables(List<String> activityTables) {
		this.activityTables = activityTables;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getLogString()
	{
		try
		{
			String startDateStr = DateUtil.formatDateSafely(new Date(startTime), "yyyy-MM-dd HH:mm:ss");
			String endDateStr = DateUtil.formatDateSafely(new Date(endTime), "yyyy-MM-dd HH:mm:ss");
			return "["+id+"] ["+version+"] ["+activityName+"] ["+startTime+"] ["+startDateStr+"] ["+endTime+"] ["+endDateStr+"] ["+activityDesc+"] ["+operationPerson+"] ["+platForm+"] ["+status+"]";
		}
		catch(Exception e)
		{
			return "[获取活动信息日志出错] ["+e+"]";
		}
	}

}


