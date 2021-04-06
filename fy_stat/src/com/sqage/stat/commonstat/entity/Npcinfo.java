package com.sqage.stat.commonstat.entity;

import java.util.Date;
/**
 * npc信息统计。主要统计npc的活动，也可以统计任务的某个事件点。
 * 
 */
public class Npcinfo {

	private Long id;
	private Date createDate;
	private String fenQu="";
	private String userName="";
	private int gameLevel;
	private String npcName="";    //npc名称，为了提高兼容性，这个名称可以是访问npc的名称，也可以设置为完成npc的任务的某个的状态点（比如：接镖，丢镖，完成押镖交镖），
	private String taskType="";   //配合npcname使用，增强兼容性。[武神传送使者的（ 报名,匹配失败、匹配成功，战斗胜利、战斗失败等）]
	private int getYOuXiBi;  //不同npc的游戏笔消耗不一样，这个值根据游戏的设置而定，或是银子或是绑银，或是积分 ，单位  （文）
	private int getWuPin;
	private int getDaoJu;
	/**
	 * 获取奖励
	 */
	private String award="";
	private String career="";        //职业
	private String jixing="";
	private String column1="";//备用扩展项
	private String column2="";//备用扩展项
	
	@Override
	public String toString() {
		return "Npcinfo [award=" + award + "] [career=" + career + "] [column1=" + column1 + "] [column2=" + column2 + "] [createDate=" + createDate
				+ "] [fenQu=" + fenQu + "] [gameLevel=" + gameLevel + "] [getDaoJu=" + getDaoJu + "] [getWuPin=" + getWuPin + "] [getYOuXiBi="
				+ getYOuXiBi + "] [id=" + id + "] [jixing=" + jixing + "] [npcName=" + npcName + "] [taskType=" + taskType + "] [userName="
				+ userName + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getFenQu() {
		return fenQu;
	}
	public void setFenQu(String fenQu) {
		this.fenQu = fenQu;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getGameLevel() {
		return gameLevel;
	}
	public void setGameLevel(int gameLevel) {
		this.gameLevel = gameLevel;
	}
	public String getNpcName() {
		return npcName;
	}
	public void setNpcName(String npcName) {
		this.npcName = npcName;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public int getGetYOuXiBi() {
		return getYOuXiBi;
	}
	public void setGetYOuXiBi(int getYOuXiBi) {
		this.getYOuXiBi = getYOuXiBi;
	}
	public int getGetWuPin() {
		return getWuPin;
	}
	public void setGetWuPin(int getWuPin) {
		this.getWuPin = getWuPin;
	}
	public int getGetDaoJu() {
		return getDaoJu;
	}
	public void setGetDaoJu(int getDaoJu) {
		this.getDaoJu = getDaoJu;
	}
	public String getAward() {
		return award;
	}
	public void setAward(String award) {
		this.award = award;
	}
	public String getCareer() {
		return career;
	}
	public void setCareer(String career) {
		this.career = career;
	}
	public String getJixing() {
		return jixing;
	}
	public void setJixing(String jixing) {
		this.jixing = jixing;
	}
	public String getColumn1() {
		return column1;
	}
	public void setColumn1(String column1) {
		this.column1 = column1;
	}
	public String getColumn2() {
		return column2;
	}
	public void setColumn2(String column2) {
		this.column2 = column2;
	}
	
	
}
