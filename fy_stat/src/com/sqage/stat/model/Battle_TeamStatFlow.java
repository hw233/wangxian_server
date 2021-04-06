package com.sqage.stat.model;

import java.io.Serializable;
import org.apache.log4j.Logger;

/**
 * 每个战队的功勋
 * 每天每战发送一次统计信息
 * 
 */
public class Battle_TeamStatFlow implements Serializable{
	
	private static final long serialVersionUID = -9203088531016193193L;
	public static Logger logger = Logger.getLogger(Battle_TeamStatFlow.class);
	
	private String		type="";       //	模式	
	private String		fenqu="";	    //	分区
	private String		guojia="";	    //	国家
	private String		gongxun="";	//	功勋值
	private String		haoTianCount="";	//	昊天人数
	private String		wuHuangCount="";	//	巫皇人数
	private String		lingZunCount="";	//	灵尊人数	
	private String		guiShaCount="";	    //	鬼煞人数
	private Long createTime=System.currentTimeMillis();
	private String column1=""; // 备用项
	private String column2=""; //备用项

	@Override
	public String toString() {
		return "Battle_TeamStatFlow [column1:" + column1 + "] [column2:" + column2 + "] [createTime:" + createTime + "] [fenqu:" + fenqu
				+ "] [gongxun:" + gongxun + "] [guiShaCount:" + guiShaCount + "] [guojia:" + guojia + "] [haoTianCount:" + haoTianCount
				+ "] [lingZunCount:" + lingZunCount + "] [type:" + type + "] [wuHuangCount:" + wuHuangCount + "]";
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFenqu() {
		return fenqu;
	}
	public void setFenqu(String fenqu) {
		this.fenqu = fenqu;
	}
	public String getGuojia() {
		return guojia;
	}
	public void setGuojia(String guojia) {
		this.guojia = guojia;
	}
	public String getGongxun() {
		return gongxun;
	}
	public void setGongxun(String gongxun) {
		this.gongxun = gongxun;
	}

	public String getHaoTianCount() {
		return haoTianCount;
	}
	public void setHaoTianCount(String haoTianCount) {
		this.haoTianCount = haoTianCount;
	}
	public String getWuHuangCount() {
		return wuHuangCount;
	}
	public void setWuHuangCount(String wuHuangCount) {
		this.wuHuangCount = wuHuangCount;
	}
	public String getLingZunCount() {
		return lingZunCount;
	}
	public void setLingZunCount(String lingZunCount) {
		this.lingZunCount = lingZunCount;
	}
	public String getGuiShaCount() {
		return guiShaCount;
	}
	public void setGuiShaCount(String guiShaCount) {
		this.guiShaCount = guiShaCount;
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
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
}
