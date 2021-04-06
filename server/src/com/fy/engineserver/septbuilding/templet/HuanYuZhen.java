package com.fy.engineserver.septbuilding.templet;

import org.w3c.dom.Element;

import com.fy.engineserver.septbuilding.service.SeptBuildingManager;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 模板实例_仙兽房
 * 
 * 
 */
@SimpleEmbeddable
public class HuanYuZhen extends SeptBuildingTemplet {

	/** 箭楼,属性箭楼的制造上限 */
	private transient int[] maxArrowTowerNum = new int[MAX_LEVEL];
	/** 可拥有的战车上限 */
	private transient int[] maxTankNum = new int[MAX_LEVEL];
	private transient String confPath;
	private static HuanYuZhen instance;

	private HuanYuZhen() {

	}

	public static HuanYuZhen getInstance() {
		return instance;
	}

	@Override
	public void loadeSpecially(Element root) {
		Element[] es = null;
		es = XmlUtil.getChildrenByName(root, "maxArrowTowerNum");
		setMaxArrowTowerNum(StringTool.string2IntArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));

		es = XmlUtil.getChildrenByName(root, "maxTankNum");
		setMaxTankNum(StringTool.string2IntArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));

	}

	public int[] getMaxArrowTowerNum() {
		return maxArrowTowerNum;
	}

	public void setMaxArrowTowerNum(int[] maxArrowTowerNum) {
		this.maxArrowTowerNum = maxArrowTowerNum;
	}

	public int[] getMaxTankNum() {
		return maxTankNum;
	}

	public void setMaxTankNum(int[] maxTankNum) {
		this.maxTankNum = maxTankNum;
	}

	@Override
	public boolean canLvUp(SeptStation station) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void initDepend() {
		// TODO Auto-generated method stub

	}

	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	public void init() throws Exception {
		
		instance = this;
		instance.setBuildingType(BuildingType.仙兽房);
		instance.setName(getBuildingType().getName());
		instance.load();
		instance.initDepend();
		SeptBuildingManager.type_templet_map.put(instance.getBuildingType(), instance);
		ServiceStartRecord.startLog(this);
	}
}
