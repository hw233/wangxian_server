package com.fy.engineserver.downcity;

public class DungeonRecord {
	long completeTime;
	
	long creatTime;
	
	String teamLeader;
	
	String[] teamMembers;
	
	String id;
	
	String dungeonName;
	
	String dungeonMapName;
	
	byte leaderPoliticalCamp;
	
	long teamLeaderId;
	
	
	public DungeonRecord(String id){
		this.id=id;
	}

	public long getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(long completeTime) {
		this.completeTime = completeTime;
	}

	public String getTeamLeader() {
		return teamLeader;
	}

	public void setTeamLeader(String teamLeader) {
		this.teamLeader = teamLeader;
	}

	public String[] getTeamMembers() {
		return teamMembers;
	}

	public void setTeamMembers(String[] teamMembers) {
		this.teamMembers = teamMembers;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDungeonName() {
		return dungeonName;
	}

	public void setDungeonName(String dungeonName) {
		this.dungeonName = dungeonName;
	}

	public String getDungeonMapName() {
		return dungeonMapName;
	}

	public void setDungeonMapName(String dungeonMapName) {
		this.dungeonMapName = dungeonMapName;
	}

	public byte getLeaderPoliticalCamp() {
		return leaderPoliticalCamp;
	}

	public void setLeaderPoliticalCamp(byte leaderPoliticalCamp) {
		this.leaderPoliticalCamp = leaderPoliticalCamp;
	}

	public long getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(long creatTime) {
		this.creatTime = creatTime;
	}

	public long getTeamLeaderId() {
		return teamLeaderId;
	}

	public void setTeamLeaderId(long teamLeaderId) {
		this.teamLeaderId = teamLeaderId;
	}

	
}
