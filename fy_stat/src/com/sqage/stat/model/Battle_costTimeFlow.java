package com.sqage.stat.model;
import java.io.Serializable;
import org.apache.log4j.Logger;
/**
 * 每场战斗用的时间
 *   每场战斗发一次统计数据
 * 
 */
public class Battle_costTimeFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;
	public static Logger logger = Logger.getLogger(Battle_costTimeFlow.class);
	
	private String		type="";   //	模式	
	private String		fenqu="";	//	道具id	
	private long		costTime;	//	战斗花费时间
	private String		haoTianCount="";	//	昊天人数
	private String		wuHuangCount="";	//	巫皇人数
	private String		lingZunCount="";	//	灵尊人数	
	private String		guiShaCount="";	    //	鬼煞人数	
	private Long createTime=System.currentTimeMillis();
	  private String column1=""; // 备用项
	  private String column2=""; //备用项

	@Override
	public String toString() {
		return "Battle_costTimeFlow [column1:" + column1 + "] [column2:" + column2 + "] [costTime:" + costTime + "] [createTime:" + createTime
				+ "] [fenqu:" + fenqu + "] [guiShaCount:" + guiShaCount + "] [haoTianCount:" + haoTianCount + "] [lingZunCount:" + lingZunCount
				+ "] [type:" + type + "] [wuHuangCount:" + wuHuangCount + "]";
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
	public long getCostTime() {
		return costTime;
	}
	public void setCostTime(long costTime) {
		this.costTime = costTime;
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
